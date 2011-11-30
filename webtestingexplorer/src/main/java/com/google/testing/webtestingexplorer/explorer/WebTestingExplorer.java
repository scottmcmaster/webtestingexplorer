/*
Copyright 2011 Google Inc. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package com.google.testing.webtestingexplorer.explorer;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openqa.selenium.WebElement;

import com.google.testing.webtestingexplorer.actions.Action;
import com.google.testing.webtestingexplorer.actions.ActionGenerator;
import com.google.testing.webtestingexplorer.actions.ActionSequence;
import com.google.testing.webtestingexplorer.config.WebTestingConfig;
import com.google.testing.webtestingexplorer.driver.WebDriverProxy;
import com.google.testing.webtestingexplorer.driver.WebDriverWrapper;
import com.google.testing.webtestingexplorer.oracles.Failure;
import com.google.testing.webtestingexplorer.oracles.FailureReason;
import com.google.testing.webtestingexplorer.oracles.Oracle;
import com.google.testing.webtestingexplorer.state.State;
import com.google.testing.webtestingexplorer.state.StateChecker;
import com.google.testing.webtestingexplorer.testcase.TestCase;
import com.google.testing.webtestingexplorer.wait.WaitCondition;

/**
 * Implements the actual test exploration process.
 * 
 * @author smcmaster@google.com (Scott McMaster)
 */
public class WebTestingExplorer {

  private final static Logger LOGGER =
      Logger.getLogger(WebTestingExplorer.class.getName());

  private WebTestingConfig config;
  private ActionGenerator actionGenerator;
  private WebDriverProxy proxy;
  
  public WebTestingExplorer(WebTestingConfig config) throws Exception {
    this.config= config;
    this.actionGenerator = new ActionGenerator(config);
    
    // Start a proxy.
    proxy = new WebDriverProxy();
  }

  public void run() throws Exception {
    // Init.
    WebDriverWrapper driver = new WebDriverWrapper(proxy);
    
    // Rip.
    Stack<ActionSequence> actionSequences = buildInitialActionSequences(driver);
    
    // Term.
    driver.getDriver().close();

    // Replay.
    replay(actionSequences, config.getMaxLength());

    // We can add another step, which is to take the generated test cases and reorder
    // actions, if the state changes are identical, the test cases are redundant.  
  }

  private Stack<ActionSequence> buildInitialActionSequences(WebDriverWrapper driver) {
    List<ActionSequence> initialActionSequences = new ArrayList<ActionSequence>(config.getInitialActionSequences());
    // Add an empty one as the default.
    initialActionSequences.add(new ActionSequence());
    
    Stack<ActionSequence> actionSequences = new Stack<ActionSequence>();
    for (ActionSequence initialActionSequence : initialActionSequences) {
      loadUrl(driver);
      for (Action action : initialActionSequence.getActions()) {
        performAction(driver, action);
      }
      List<Action> actions = getAllPossibleActionsInCurrentState(driver);
      for (Action action : actions) {
        ActionSequence sequence = new ActionSequence(action);
        actionSequences.push(sequence);
      }
      driver.getDriver().close();
    }
    return actionSequences;
  }

  private void performAction(WebDriverWrapper driver, Action action) {
    // We should reset the proxy on each action (keeping in mind that we
    // don't really know which actions will actually trigger http
    // request/responses.
    proxy.resetForRequest();
    
    action.perform(driver);
    waitOnConditions(config.getAfterActionWaitConditions(), driver);
  }

  private void loadUrl(WebDriverWrapper driver) {
    driver.get(config.getUrl(), config.getInitialWaitConditions());
  }

  /**
   * Waits for the given list of conditions to be true before returning.
   * TODO(smcmaster): Make the wait between checks configurable, and add a timeout.
   */
  private void waitOnConditions(List<WaitCondition> waitConditions, WebDriverWrapper driver) {
    for (WaitCondition waitCondition : waitConditions) {
      waitCondition.reset();
    }
    while (true) {
      boolean allCanContinue = true;
      String conditionDescription = "";
      for (WaitCondition waitCondition : waitConditions) {
        if (!waitCondition.canContinue(driver)) {
          allCanContinue = false;
          conditionDescription = waitCondition.getDescription();
          break;
        }
      }
      if (allCanContinue) {
        break;
      }
      System.out.println("Waiting for " + conditionDescription);
      try {
        Thread.sleep(1000);
      } catch (InterruptedException useless) {
      }
    }
  }

  private List<Action> getAllPossibleActionsInCurrentState(WebDriverWrapper driver) {
    List<Action> actions = new ArrayList<Action>();
    List<WebElement> allElements = driver.getAllElements();
    int elementIndex = 0;
    for (WebElement element : allElements) {
      List<Action> newActions = actionGenerator.generateActionsForElement(elementIndex, element);
      actions.addAll(newActions);
      ++elementIndex;
    }    
    return actions;
  }

  // As long as the test case is longer than the previous one, you don't need to
  // restart the browser.
  private void replay(Stack<ActionSequence> actionSequences, int maxSequenceLength) throws Exception {
    int testCaseCount = 0;
    while (!actionSequences.isEmpty()) {
      ActionSequence actionSequence = actionSequences.pop();
      ++testCaseCount;
      LOGGER.info("" + testCaseCount + ": " + actionSequence.toString());
      WebDriverWrapper driver = new WebDriverWrapper(proxy);
      List<State> stateBeforeLastAction = runActionSequence(actionSequence, driver);

      // Check the state and add a new test case if it has changed.
      List<State> finalState = createStateSnapshot(driver);   
      if (!finalState.equals(stateBeforeLastAction)) {
        if (config.getTestCaseWriter() != null) {
          TestCase testCase = new TestCase(config.getUrl(), actionSequence);
          config.getTestCaseWriter().writeTestCase(testCase, "test-" + testCaseCount + ".xml");
        }
      }
      
      // I wonder if there is a better way to identify test cases than to check just
      // initial vs. final state. As it is, we generate lots of redundant test cases
      // (for example, on the feedback form, each additional repeated click of the submit
      // button results in a new test case)...
      
      // Options for checking state:
      //    Need to ignore the element we just took an action on.
      //    Look for new or removed elements.
      //    Look at some CSS properties (disabled, color, etc.) for some subset of elements.
      //        All elements? Elements with ids/names?
      if (actionSequence.getLength() < maxSequenceLength) {
        // Extend.
        List<Action> actions = getAllPossibleActionsInCurrentState(driver);
        for (Action action : actions) {
          ActionSequence extendedSequence = new ActionSequence(actionSequence);
          extendedSequence.addAction(action);
          actionSequences.push(extendedSequence);
        }
      }
      driver.getDriver().close();
    }
  }

  /**
   * Executes the given action sequence.
   * 
   * @return the state prior to the last action being executed.
   */
  private List<State> runActionSequence(ActionSequence actionSequence, WebDriverWrapper driver) {
    loadUrl(driver);
    
    List<State> stateBeforeLastAction = null;
    for (int i = 0; i < actionSequence.getActions().size(); ++i) {
      Action action = actionSequence.getActions().get(i);
      // If this is the last action, grab the state before and after.
      if (i == actionSequence.getActions().size() - 1) {
        stateBeforeLastAction = createStateSnapshot(driver);
      }
      performAction(driver, action);
      
      // Check for failures.
      checkForFailures(config.getAfterActionOracles(), driver, actionSequence, action);
    }
    
    // Check for failures.
    checkForFailures(config.getFinalOracles(), driver, actionSequence,
        actionSequence.getLastAction());
    return stateBeforeLastAction;
  }

  /**
   * Checks the given oracles for failures.
   */
  private void checkForFailures(List<Oracle> oracles,
      WebDriverWrapper driver,
      ActionSequence actionSequence,
      Action action) {
    List<FailureReason> failureReasons = new ArrayList<FailureReason>();
    for (Oracle oracle : oracles) {
      failureReasons.addAll(oracle.check(driver));
    }
    if (!failureReasons.isEmpty()) {
      // Create a failure.
      Failure failure = new Failure(actionSequence, action);
      failure.addReasons(failureReasons);
      LOGGER.log(Level.INFO, "Failure detected: " + failure);
    }
  }

  /**
   * Takes a snapshot of the current state using each of the configured
   * checkers.
   * 
   * @param driver
   * @return a list of states parallel to the list of checkers.
   */
  private List<State> createStateSnapshot(WebDriverWrapper driver) {
    List<State> stateList = new ArrayList<State>();
    for (StateChecker stateChecker : config.getStateCheckers()) {
      stateList.add(stateChecker.createState(driver, null));
    }
    return stateList;
  }
}
