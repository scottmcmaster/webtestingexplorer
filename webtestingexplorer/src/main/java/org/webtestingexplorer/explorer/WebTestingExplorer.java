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
package org.webtestingexplorer.explorer;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import org.openqa.selenium.WebElement;
import org.webtestingexplorer.actions.Action;
import org.webtestingexplorer.actions.ActionGenerator;
import org.webtestingexplorer.actions.ActionSequence;
import org.webtestingexplorer.actions.BackAction;
import org.webtestingexplorer.actions.ForwardAction;
import org.webtestingexplorer.actions.RefreshAction;
import org.webtestingexplorer.config.ActionSequenceFilter;
import org.webtestingexplorer.config.EquivalentWebElementsSet;
import org.webtestingexplorer.config.WebTestingConfig;
import org.webtestingexplorer.driver.ActionSequenceRunner;
import org.webtestingexplorer.driver.ActionSequenceRunnerConfig;
import org.webtestingexplorer.driver.WebDriverWrapper;
import org.webtestingexplorer.driver.ActionSequenceRunner.ActionSequenceResult;
import org.webtestingexplorer.driver.ActionSequenceRunner.BeforeActionCallback;
import org.webtestingexplorer.identifiers.WebElementWithIdentifier;
import org.webtestingexplorer.state.State;
import org.webtestingexplorer.state.StateChange;
import org.webtestingexplorer.state.StateChecker;
import org.webtestingexplorer.testcase.TestCase;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implements the actual test exploration process.
 * The default behavior when exploring is to run the longest discovered
 * action sequences first.
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
    this.runner = new ActionSequenceRunner(config.getWebDriverFactory());
  }

  public void run() throws Exception {
    // Rip.
    Deque<ActionSequence> actionSequences = buildInitialActionSequences();
    
    // Replay.
    replay(actionSequences, config.getMaxLength());

    // We can add another step, which is to take the generated test cases and reorder
    // actions, if the state changes are identical, the test cases are redundant.
    
    runner.shutdown();
  }

  private Deque<ActionSequence> buildInitialActionSequences() throws Exception {
    List<ActionSequence> initialActionSequences = Lists.newArrayList(config.getInitialActionSequences());
    
    Deque<ActionSequence> actionSequences = new ArrayDeque<ActionSequence>();
    for (ActionSequence initialActionSequence : initialActionSequences) {
      runner.runActionSequence(new ActionSequenceRunnerConfig(
          config.getUrl(),
          initialActionSequence,
          config.getOracleConfig(),
          config.getWaitConditionConfig(),
          null,
          config.getActionableWebElementSelector(),
          config.getStatefulWebElementSelector(),
          config.getNumRetries()));
      List<Action> actions = getAllPossibleActionsInCurrentState();
      for (Action action : actions) {
        ActionSequence sequence = new ActionSequence(initialActionSequence);
        sequence.addAction(action);
        checkPushActionSequence(actionSequences, sequence);
      }
      runner.getDriver().close();
    }
    return actionSequences;
  }

  private List<Action> getAllPossibleActionsInCurrentState() {
    LOGGER.info("Getting actions from " + runner.getDriver().getDriver().getCurrentUrl());
    
    List<Action> actions = Lists.newArrayList();

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

    // Look for element actions, filtering out ones in the same equivalence
    // classes.
    Map<EquivalentWebElementsSet, Boolean> markedEquivalentSets = Maps.newHashMap();
    for (EquivalentWebElementsSet equivalentSet : config.getEquivalentWebElementSets()) {
      markedEquivalentSets.put(equivalentSet, false);
    }
    
    List<WebElementWithIdentifier> allElements = runner.getDriver().getActionableElements();
    for (WebElementWithIdentifier elementWithId : allElements) {
      boolean shouldAddActions = checkEquivalentElementSets(markedEquivalentSets,
          elementWithId);
      
      if (shouldAddActions) {
        Set<Action> newActions = actionGenerator.generateActionsForElement(
            runner.getDriver(), elementWithId);
        actions.addAll(newActions);
      }
    }
    
    LOGGER.info("All possible actions in current state: " + actions.toString());
    return actions;
  }

  /**
   * Checks to see if a given web element is equivalent to one we have already added
   * an action for.
   * 
   * @param markedEquivalentSets tracks the equivalent sets that have already been addressed.
   * @param elementWithId the element to check for equivalence to already-added elements.
   * @return true if we should add actions for the element, false if we should skip it.
   */
  private boolean checkEquivalentElementSets(
      Map<EquivalentWebElementsSet, Boolean> markedEquivalentSets,
      WebElementWithIdentifier elementWithId) {
    
    boolean shouldAddActions = true;
    
    for (EquivalentWebElementsSet equivalentSet : config.getEquivalentWebElementSets()) {
      Set<WebElement> equivalentElements = equivalentSet.getEquivalentElements(runner.getDriver());
      
      // TODO(smcmaster): There are a couple of untested assumptions here.
      // First, that all object ref's for WebElements retrieved from the driver
      // which are not stale, are the same instances. This seems reasonable since
      // WebDriver caches them.
      // Second, that this will work correctly across frames. That part, I kind of
      // doubt. We may need to require that EquivalentElementSets only target
      // a single frame and make that explicit.
      
      if (equivalentElements.contains(elementWithId.safeGetElement(runner.getDriver()))) {
        if (markedEquivalentSets.containsKey(equivalentSet)) {
          // We have already added an element from this set.
          shouldAddActions = false;
        }
        // Mark that we will add an element from this set so that we don't add
        // any more.
        markedEquivalentSets.put(equivalentSet, true);        
      }
    }
    
    return shouldAddActions;
  }

  // As long as the test case is longer than the previous one, you don't need to
  // restart the browser.
  private void replay(Deque<ActionSequence> actionSequences, int maxSequenceLength) throws Exception {
    int testCaseCount = 0;
    int failedCaseCount = 0;
    int errorCaseCount = 0;
    while (!actionSequences.isEmpty()) {
      LOGGER.info("Current queue size: " + actionSequences.size());
      final ActionSequence actionSequence = actionSequences.pop();
      ++testCaseCount;
      LOGGER.info("" + testCaseCount + ": " + actionSequence.toString());
      try {
        final StateChange stateChange = new StateChange();
        ActionSequenceResult result = runner.runActionSequence(new ActionSequenceRunnerConfig(
            config.getUrl(),
            actionSequence,
            config.getOracleConfig(),
            config.getWaitConditionConfig(),
            new BeforeActionCallback() {
                @Override
                public void onBeforeAction(Action action) {
                  if (action == actionSequence.getLastAction()) {
                    stateChange.setBeforeState(createStateSnapshot(runner.getDriver()));
                  }
                }
               },
            config.getActionableWebElementSelector(),
            config.getStatefulWebElementSelector(),
            config.getNumRetries()));
        if (result.hasErrors()) {
          ++failedCaseCount;
        }
        
        // Check the state and add a new test case if it has changed.
        stateChange.setAfterState(createStateSnapshot(runner.getDriver()));   
        if (stateChange.isStateChanged()) {
          if (config.getTestCaseWriter() != null) {
            writeTestCase(testCaseCount, actionSequence, stateChange.getAfterState());
          }
        }
        
        // Options for checking state:
        //    Need to ignore the element we just took an action on.
        //    Look for new or removed elements.
        //    Look at some CSS properties (disabled, color, etc.) for some subset of elements.
        //        All elements? Elements with ids/names?
        if (actionSequence.getLength() < maxSequenceLength) {
          // Extend.
          List<Action> actions = getAllPossibleActionsInCurrentState();
          for (Action action : actions) {
            ActionSequence extendedSequence = new ActionSequence(actionSequence);
            extendedSequence.addAction(action);
            checkPushActionSequence(actionSequences, extendedSequence);
          }
        }
        
        if (config.getActionSequencePrioritizer() != null) {
          actionSequences = config.getActionSequencePrioritizer().prioritize(actionSequences);
        }
        LOGGER.info("Current queue length: " + actionSequences.size());
      } catch (Exception e) {
        LOGGER.log(Level.SEVERE, "Error running action sequence, out of retries");
        ++errorCaseCount;
      } finally {
        try { runner.getDriver().close(); } catch (Exception e) {}
      }
      
      LOGGER.info(String.format("Run: %d, Failed: %d, Errors: %d",
          testCaseCount, failedCaseCount, errorCaseCount));
    }
  }

  /**
   * Creates and writes out a test case from the given action sequence.
   */
  private void writeTestCase(int testCaseCount, final ActionSequence actionSequence,
      List<State> finalState) {
    String oracleConfigFactoryClassName = null;
    if (config.getOracleConfigFactory() != null) {
      oracleConfigFactoryClassName = config.getOracleConfigFactory().getClass().getName();
    }
    
    String waitConditionConfigFactoryClassName = null;
    if (config.getWaitConditionConfigFactory() != null) {
      waitConditionConfigFactoryClassName =
          config.getWaitConditionConfigFactory().getClass().getName();
    }
    
    String actionableWebElementSelectorFactoryClassName = null;
    if (config.getActionableWebElementSelectorFactory() != null) {
      actionableWebElementSelectorFactoryClassName =
          config.getActionableWebElementSelectorFactory().getClass().getName();
    }
    
    String statefulWebElementSelectorFactoryClassName = null;
    if (config.getStatefulWebElementSelectorFactory() != null) {
      statefulWebElementSelectorFactoryClassName =
          config.getStatefulWebElementSelectorFactory().getClass().getName();
    }
    
    TestCase testCase = new TestCase(config.getUrl(), actionSequence, finalState,
        oracleConfigFactoryClassName, waitConditionConfigFactoryClassName,
        actionableWebElementSelectorFactoryClassName, statefulWebElementSelectorFactoryClassName);
    config.getTestCaseWriter().writeTestCase(testCase, "test-" + testCaseCount + ".xml");
  }

  /**
   * Adds the given action sequence to the queue to be run assuming it passes
   * all filtering.
   */
  private void checkPushActionSequence(Deque<ActionSequence> actionSequences,
      ActionSequence sequence) {
    for (ActionSequenceFilter filter : config.getActionSequenceFilters()) {
      if (filter.shouldExplore(sequence, actionSequences)) {
        actionSequences.push(sequence);
      }
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
      stateList.add(stateChecker.createState(driver));
    }
    return stateList;
  }
}