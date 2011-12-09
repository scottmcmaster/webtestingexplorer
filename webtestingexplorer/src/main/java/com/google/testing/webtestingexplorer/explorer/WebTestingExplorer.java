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

import com.google.testing.webtestingexplorer.actions.Action;
import com.google.testing.webtestingexplorer.actions.ActionGenerator;
import com.google.testing.webtestingexplorer.actions.ActionSequence;
import com.google.testing.webtestingexplorer.actions.BackAction;
import com.google.testing.webtestingexplorer.actions.ForwardAction;
import com.google.testing.webtestingexplorer.actions.RefreshAction;
import com.google.testing.webtestingexplorer.config.WebTestingConfig;
import com.google.testing.webtestingexplorer.driver.ActionSequenceRunner;
import com.google.testing.webtestingexplorer.driver.ActionSequenceRunner.BeforeActionCallback;
import com.google.testing.webtestingexplorer.driver.WebDriverWrapper;
import com.google.testing.webtestingexplorer.state.State;
import com.google.testing.webtestingexplorer.state.StateChange;
import com.google.testing.webtestingexplorer.state.StateChecker;
import com.google.testing.webtestingexplorer.testcase.TestCase;

import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.logging.Logger;

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
  private ActionSequenceRunner runner;
  
  public WebTestingExplorer(WebTestingConfig config) throws Exception {
    this.config = config;
    this.actionGenerator = new ActionGenerator(config);
    this.runner = new ActionSequenceRunner(config.getOracleConfig(),
        config.getWaitConditionConfig());
  }

  public void run() throws Exception {
    // Init.
    WebDriverWrapper driver = new WebDriverWrapper(runner.getProxy());
    
    // Rip.
    Stack<ActionSequence> actionSequences = buildInitialActionSequences(driver);
    
    // Term.
    driver.close();

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
      runner.runActionSequence(config.getUrl(), initialActionSequence, driver, null);
      List<Action> actions = getAllPossibleActionsInCurrentState(driver);
      for (Action action : actions) {
        ActionSequence sequence = new ActionSequence(action);
        actionSequences.push(sequence);
      }
      driver.close();
    }
    return actionSequences;
  }

  private List<Action> getAllPossibleActionsInCurrentState(WebDriverWrapper driver) {
    List<Action> actions = new ArrayList<Action>();

    // Look for browser actions.
    if (config.isUseBackButtonAction()) {
      actions.add(new BackAction());
    }
    if (config.isUseForwardButtonAction()) {
      actions.add(new ForwardAction());
    }
    if (config.isUseRefreshButtonAction()) {
      actions.add(new RefreshAction());
    }

    // Look for element actions.
    List<WebElement> allElements = driver.getAllElements();
    int elementIndex = 0;
    for (WebElement element : allElements) {
      List<Action> newActions = actionGenerator.generateActionsForElement(
          driver, elementIndex, element);
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
      final ActionSequence actionSequence = actionSequences.pop();
      ++testCaseCount;
      LOGGER.info("" + testCaseCount + ": " + actionSequence.toString());
      final WebDriverWrapper driver = new WebDriverWrapper(runner.getProxy());
      final StateChange stateChange = new StateChange();
      runner.runActionSequence(config.getUrl(), actionSequence, driver, new BeforeActionCallback() {
        @Override
        public void onBeforeAction(Action action) {
          if (action == actionSequence.getLastAction()) {
            stateChange.setBeforeState(createStateSnapshot(driver));
          }
        }
      });

      // Check the state and add a new test case if it has changed.
      stateChange.setAfterState(createStateSnapshot(driver));   
      if (stateChange.isStateChanged()) {
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
      driver.close();
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
