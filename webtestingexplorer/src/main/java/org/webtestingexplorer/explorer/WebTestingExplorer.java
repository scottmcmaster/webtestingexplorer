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
import com.google.common.collect.Sets;

import org.openqa.selenium.WebElement;
import org.webtestingexplorer.actions.Action;
import org.webtestingexplorer.actions.ActionGenerator;
import org.webtestingexplorer.actions.ActionSequence;
import org.webtestingexplorer.actions.ActionSequenceQueue;
import org.webtestingexplorer.actions.BackAction;
import org.webtestingexplorer.actions.ForwardAction;
import org.webtestingexplorer.actions.RefreshAction;
import org.webtestingexplorer.config.ActionGeneratorConfig;
import org.webtestingexplorer.config.ActionSequenceFilter;
import org.webtestingexplorer.config.WebElementSelector;
import org.webtestingexplorer.config.WebElementSelectorRegistry;
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
import org.webtestingexplorer.testcase.TestCaseConfig;
import org.webtestingexplorer.testcase.TestCaseWriter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
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
    this.actionGenerator = new ActionGenerator(config.isUseDefaultActionGeneratorConfigs());
    this.runner = new ActionSequenceRunner(config.getWebDriverFactory());
  }

  public void run() throws Exception {
    // Rip.
    ActionSequenceQueue actionSequences = null;
    if (config.getQueueFilename() != null && !config.getQueueFilename().isEmpty()) {
      try {
        actionSequences = ActionSequenceQueue.readFromFile(config.getQueueFilename());
      } catch (Exception ex) {
        LOGGER.log(Level.WARNING, "Failed to load " + config.getQueueFilename()
            + ", starting over");
      }
    }
    if (actionSequences == null || actionSequences.isEmpty()) {
      actionSequences = buildInitialActionSequences(
          config.getNumPartitions(), config.getPartitionNumber());
    }
    
    // Replay.
    replay(actionSequences, config.getMaxLength());

    // We can add another step, which is to take the generated test cases and reorder
    // actions, if the state changes are identical, the test cases are redundant.
    
    runner.shutdown();
  }

  private ActionSequenceQueue buildInitialActionSequences(
      int numPartitions, int partitionNumber) throws Exception {
    List<ActionSequence> initialActionSequences = Lists.newArrayList(config.getInitialActionSequences());
    
    ActionSequenceQueue actionSequences = new ActionSequenceQueue();
    for (ActionSequence initialActionSequence : initialActionSequences) {
      runner.runActionSequence(new ActionSequenceRunnerConfig(
          config.getUrl(),
          initialActionSequence,
          config.getOracleConfig(),
          config.getWaitConditionConfig(),
          null,
          config.getNumRetries(),
          config.isUseElementsCache(),
          false));
      List<Action> actions = getAllPossibleActionsInCurrentState();
      for (Action action : actions) {
        extendAndPushActionSequence(actionSequences, initialActionSequence, action);
      }
      runner.getDriver().close();
    }
    
    if (numPartitions == 0) {
      // We are not using partitioning of the initial action sequences.
      return actionSequences;
    }
    
    int partitionSize = actionSequences.size() / numPartitions;
    if (actionSequences.size() % numPartitions != 0) {
      ++partitionSize;
    }
    List<List<ActionSequence>> partitionedActionSequences = Lists.partition(
        actionSequences.asList(), partitionSize);
    return new ActionSequenceQueue(partitionedActionSequences.get(partitionNumber));
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

    // Look for element actions, filtering out ones in the same equivalence classes.
    Set<String> markedEquivalentSelectors = Sets.newHashSet();
    Map<String, Set<WebElement>> cachedEquivalentElements = cacheEquivalentElements();
    
    List<WebElementWithIdentifier> allElements = runner.getDriver().getActionableElements();
    for (WebElementWithIdentifier elementWithId : allElements) {
      boolean shouldAddActions = checkEquivalentElementSelectors(
          cachedEquivalentElements,
          markedEquivalentSelectors,
          elementWithId);
      
      if (shouldAddActions) {
        List<ActionGeneratorConfig> actionGeneratorConfigs = Lists.newArrayList();
        for (ActionGeneratorConfig actionGeneratorConfig : config.getActionGeneratorConfigs()) {
          if (actionGeneratorConfig.isActive(runner.getDriver())) {
            actionGeneratorConfigs.add(actionGeneratorConfig);
          }
        }
        Set<Action> newActions = actionGenerator.generateActionsForElement(
            runner.getDriver(), actionGeneratorConfigs, elementWithId);
        actions.addAll(newActions);
      }
    }
    
    LOGGER.info("All possible actions in current state: " + actions.toString());
    return actions;
  }

  /**
   * @return a map of selector name -> elements returned by that selector for the
   *    elements defined as equivalent in the config.
   */
  private Map<String, Set<WebElement>> cacheEquivalentElements() {
    Map<String, Set<WebElement>> cachedEquivalentElements = Maps.newHashMap();
    for (Map.Entry<String, WebElementSelector> equivalentSelector : config.getEquivalentWebElementSelectors().entrySet()) {
      Set<WebElement> equivalentElements = Sets.newLinkedHashSet(equivalentSelector.getValue().select(runner.getDriver().getDriver()));
      cachedEquivalentElements.put(equivalentSelector.getKey(), equivalentElements);
    }
    return cachedEquivalentElements;
  }

  /**
   * Checks to see if a given web element is equivalent to one we have already added
   * an action for.
   * 
   * @param cachedEquivalentElements the equivalent elements by selector name in the current state.
   * @param markedEquivalentSelectors tracks the equivalent selectors that have already been addressed.
   * @param elementWithId the element to check for equivalence to already-added elements.
   * @return true if we should add actions for the element, false if we should skip it.
   */
  private boolean checkEquivalentElementSelectors(
      Map<String, Set<WebElement>> cachedEquivalentElements,
      Set<String> markedEquivalentSelectors,
      WebElementWithIdentifier elementWithId) {
    
    boolean shouldAddActions = true;
    
    for (Map.Entry<String, Set<WebElement>> equivalentElements : cachedEquivalentElements.entrySet()) {
      
      if (equivalentElements.getValue().contains(elementWithId.safeGetElement(runner.getDriver())) &&
        markedEquivalentSelectors.contains(equivalentElements.getKey())) {
          // We have already added an element from this set.
          LOGGER.info("Filtering an element from equivalent selector: " + equivalentElements.getKey());
          shouldAddActions = false;
      }
      // Mark that we will add an element from this set so that we don't add
      // any more.
      markedEquivalentSelectors.add(equivalentElements.getKey());        
    }
    
    return shouldAddActions;
  }

  // As long as the test case is longer than the previous one, you don't need to
  // restart the browser.
  private void replay(ActionSequenceQueue actionSequences, int maxSequenceLength) throws Exception {
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
            config.getNumRetries(),
            config.isUseElementsCache(),
            config.isCaptureScreenshots()));
        if (result.hasFailures()) {
          ++failedCaseCount;
        }
        
        // Check the state and add a new test case if it has changed.
        stateChange.setAfterState(createStateSnapshot(runner.getDriver()));   
        if (stateChange.isStateChanged()) {
          writeTestCase(actionSequence, stateChange.getAfterState(), result);
        }
        
        // Options for checking state:
        //    Need to ignore the element we just took an action on.
        //    Look for new or removed elements.
        //    Look at some CSS properties (disabled, color, etc.) for some subset of elements.
        //        All elements? Elements with ids/names?
        // TODO(smcmaster): I wonder if the initial/final action sequences should really count toward the max length check.
        if (actionSequence.getLength() < maxSequenceLength) {
          // Extend.
          List<Action> actions = getAllPossibleActionsInCurrentState();
          for (Action action : actions) {
            extendAndPushActionSequence(actionSequences, actionSequence, action);
          }
        }
        
        if (config.getActionSequencePrioritizer() != null) {
          actionSequences = config.getActionSequencePrioritizer().prioritize(actionSequences);
        }
        LOGGER.info("Current queue length: " + actionSequences.size());
        if (config.getQueueFilename() != null && !config.getQueueFilename().isEmpty()) {
          ActionSequenceQueue.writeToFile(actionSequences, config.getQueueFilename());
        }
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
   * Extends the given action sequence with the given action, appending final
   * action sequences as necessary.
   */
  private void extendAndPushActionSequence(
      ActionSequenceQueue actionSequences, final ActionSequence actionSequence, Action action) {
    ActionSequence extendedSequence = new ActionSequence(actionSequence);
    extendedSequence.addAction(action);
    if (!config.getFinalActionSequences().isEmpty()) {
      for (ActionSequence finalActionSequence : config.getFinalActionSequences()) {
        ActionSequence extendedSequenceWithFinal = new ActionSequence(extendedSequence);
        extendedSequenceWithFinal.addActionsFrom(finalActionSequence);
        checkPushActionSequence(actionSequences, extendedSequenceWithFinal);
      }
    } else {
      // Push the action sequence as-is.
      checkPushActionSequence(actionSequences, extendedSequence);
    }
  }

  /**
   * Creates and writes out a test case from the given action sequence.
   */
  private void writeTestCase(final ActionSequence actionSequence,
      List<State> finalState, ActionSequenceResult result) {
    String oracleConfigFactoryClassName = null;
    if (config.getOracleConfigFactory() != null) {
      oracleConfigFactoryClassName = config.getOracleConfigFactory().getClass().getName();
    }
    
    String waitConditionConfigFactoryClassName = null;
    if (config.getWaitConditionConfigFactory() != null) {
      waitConditionConfigFactoryClassName =
          config.getWaitConditionConfigFactory().getClass().getName();
    }
    
    TestCase testCase = new TestCase(config.getUrl(), actionSequence, finalState,
        oracleConfigFactoryClassName, waitConditionConfigFactoryClassName,
        buildTestCaseConfig());
    String testCaseId = UUID.randomUUID().toString();
    for (TestCaseWriter testCaseWriter : config.getTestCaseWriters()) {
      testCaseWriter.writeTestCase(testCase, testCaseId, result);
    }
  }

  /**
   * @return a {@link TestCaseConfig} based on the current state of the explorer.
   */
  private TestCaseConfig buildTestCaseConfig() {
    TestCaseConfig result = new TestCaseConfig(WebElementSelectorRegistry.getInstance());
    return result;
  }

  /**
   * Adds the given action sequence to the queue to be run assuming it passes
   * all filtering.
   */
  private void checkPushActionSequence(ActionSequenceQueue actionSequences,
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
