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
package org.webtestingexplorer.config;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import org.webtestingexplorer.actions.ActionSequence;
import org.webtestingexplorer.config.waitcondition.WaitConditionConfig;
import org.webtestingexplorer.config.waitcondition.WaitConditionConfigFactory;
import org.webtestingexplorer.driver.FirefoxWebDriverFactory;
import org.webtestingexplorer.driver.WebDriverFactory;
import org.webtestingexplorer.state.StateChecker;
import org.webtestingexplorer.testcase.TestCaseWriter;

import java.util.List;
import java.util.Map;

/**
 * @author smcmaster@google.com (Scott McMaster)
 */
public class WebTestingConfig {
  private String url;
  private List<TestCaseWriter> testCaseWriters = Lists.newArrayList();
  private int maxLength;
  private List<StateChecker> stateCheckers = Lists.newArrayList();
  private List<ActionGeneratorConfig> actionGeneratorConfigs = Lists.newArrayList();
  private boolean useBackButtonAction;
  private boolean useForwardButtonAction;
  private boolean useRefreshButtonAction;
  private List<ActionSequenceFilter> actionSequenceFilters = Lists.newArrayList();
  private WebDriverFactory driverFactory = new FirefoxWebDriverFactory();
  private ActionSequencePrioritizer actionSequencePrioritizer;
  private int numRetries = 3;
  private String queueFilename;
  private boolean captureScreenshots;
  
  /**
   * The partition number for parallelizing the generation process. Currently this
   * is only used to choose a partition from the initial action sequences and
   * is completely ignored if you are starting from an existing action sequence queue.
   * If numPartitions is 0 this value is ignored.
   */
  private int partitionNumber;
  
  /**
   * How many partitions we're using. May be the default of 0.
   */
  private int numPartitions;
  
  /**
   * Whether or not to try to use cached elements in the driver wrapper.
   */
  private boolean useElementsCache;

  /**
   * Map of selector description to a {@link WebElementSelector} the selected elements of
   * which are equivalent for the purposes of testing.
   * TODO(smcmaster): Just store these by selector name and pull them out of the registry?
   */
  private Map<String, WebElementSelector> equivalentWebElementSelectors = Maps.newLinkedHashMap();
  
  /**
   * The wait condition configuration factory.
   */
  private WaitConditionConfigFactory waitConditionConfigFactory;
  
  /**
   * The oracle configuration factory.
   */
  private OracleConfigFactory oracleConfigFactory;
  
  /**
   * A list of action sequences we execute to get into the interesting
   * initial state(s) to start exploration.
   * Has an empty action sequence for a default; if you don't want to explore
   * starting from empty, you should explicitly remove this in your setup.
   */
  private List<ActionSequence> initialActionSequences = Lists.newArrayList(new ActionSequence());
  
  /**
   * A list of action sequences to run at the end of every generated action sequence.
   * This is useful if you are testing something like web search and you want to
   * always click the "search" button last-thing. Another example is a dialog with
   * OK and Cancel buttons, and you want to generate all sequences ending in
   * one or the other. You can also include an empty action sequence if you want
   * to generate action sequence permutations that DO NOT include explicit final actions.
   */
  private List<ActionSequence> finalActionSequences = Lists.newArrayList();
  
  /** Whether or not to use the default action generator configs, defaults to true. */
  private boolean useDefaultActionGeneratorConfigs = true;
  
  public WebTestingConfig() {
  }
  
  public WaitConditionConfig getWaitConditionConfig() {
    if (waitConditionConfigFactory == null) {
      return null;
    }
    return waitConditionConfigFactory.createWaitConditionConfig();
  }
  
  public WaitConditionConfigFactory getWaitConditionConfigFactory() {
    return waitConditionConfigFactory;
  }
  
  public WebTestingConfig setWaitConditionConfigFactory(
      WaitConditionConfigFactory waitConditionConfigFactory) {
    this.waitConditionConfigFactory = waitConditionConfigFactory;
    return this;
  }
  
  public OracleConfig getOracleConfig() {
    if (oracleConfigFactory == null) {
      return null;
    }
    return oracleConfigFactory.createOracleConfig();
  }
  
  public OracleConfigFactory getOracleConfigFactory() {
    return oracleConfigFactory;
  }
  
  public WebTestingConfig setOracleConfigFactory(OracleConfigFactory oracleConfigFactory) {
    this.oracleConfigFactory = oracleConfigFactory;
    return this;
  }
  
  public WebTestingConfig setUrl(String url) {
    this.url = url;
    return this;
  }
  
  public String getUrl() {
    return url;
  }

  public WebTestingConfig setMaxLength(int maxLength) {
    this.maxLength = maxLength;
    return this;
  }
  
  public int getMaxLength() {
    return maxLength;
  }

  public List<StateChecker> getStateCheckers() {
    return stateCheckers;
  }

  public WebTestingConfig addStateChecker(StateChecker stateChecker) {
    stateCheckers.add(stateChecker);
    return this;
  }
  
  public List<ActionGeneratorConfig> getActionGeneratorConfigs() {
    return actionGeneratorConfigs;
  }
  
  public WebTestingConfig addActionGeneratorConfig(ActionGeneratorConfig config) {
    actionGeneratorConfigs.add(config);
    return this;
  }
  
  public List<ActionSequenceFilter> getActionSequenceFilters() {
    return actionSequenceFilters;
  }
  
  public WebTestingConfig addActionSequenceFilter(ActionSequenceFilter filter) {
    actionSequenceFilters.add(filter);
    return this;
  }
  
  public Map<String, WebElementSelector> getEquivalentWebElementSelectors() {
    return equivalentWebElementSelectors;
  }
  
  public WebTestingConfig addEquivalentWebElementSelector(String description, WebElementSelector selector) {
    if (equivalentWebElementSelectors.containsKey(description)) {
      throw new IllegalStateException("Duplicate equivalent element selector: " + description);
    }
    equivalentWebElementSelectors.put(description, selector);
    return this;
  }
  
  public List<ActionSequence> getInitialActionSequences() {
    return initialActionSequences;
  }
  
  public WebTestingConfig clearInitialActionSequences() {
    initialActionSequences.clear();
    return this;
  }
  
  public WebTestingConfig addInitialActionSequence(ActionSequence initialActionSequence) {
    initialActionSequence.setInitial();
    initialActionSequences.add(initialActionSequence);
    return this;
  }
  
  public List<ActionSequence> getFinalActionSequences() {
    return finalActionSequences;
  }
  
  public WebTestingConfig clearFinalActionSequences() {
    finalActionSequences.clear();
    return this;
  }
  
  public WebTestingConfig addFinalActionSequence(ActionSequence finalActionSequence) {
    finalActionSequences.add(finalActionSequence);
    return this;
  }
  
  public WebTestingConfig addTestCaseWriter(TestCaseWriter testCaseWriter) {
    testCaseWriters.add(testCaseWriter);
    return this;
  }
  
  public List<TestCaseWriter> getTestCaseWriters() {
    return testCaseWriters;
  }
  
  public boolean isUseBackButtonAction() {
    return useBackButtonAction;
  }
  
  public WebTestingConfig withBackButtonAction() {
    useBackButtonAction = true;
    return this;
  }
  
  public boolean isUseForwardButtonAction() {
    return useForwardButtonAction;
  }
  
  public WebTestingConfig withForwardButtonAction() {
    useForwardButtonAction = true;
    return this;
  }
  
  public boolean isUseRefreshButtonAction() {
    return useRefreshButtonAction;
  }
  
  public WebTestingConfig withRefreshButtonAction() {
    useRefreshButtonAction = true;
    return this;
  }
  
  public WebDriverFactory getWebDriverFactory() {
    return driverFactory;
  }
  
  public WebTestingConfig setWebDriverFactory(WebDriverFactory driverFactory) {
    this.driverFactory = driverFactory;
    return this;
  }
  
  public int getNumRetries() {
    return numRetries;
  }
  
  public WebTestingConfig setNumRetries(int numRetries) {
    this.numRetries = numRetries;
    return this;
  }

  public ActionSequencePrioritizer getActionSequencePrioritizer() {
    return actionSequencePrioritizer;
  }
  
  public WebTestingConfig setActionSequencePrioritizer(
      ActionSequencePrioritizer actionSequencePrioritizer) {
    this.actionSequencePrioritizer = actionSequencePrioritizer;
    return this;
  }
  
  public WebTestingConfig setQueueFilename(String queueFileName) {
    this.queueFilename = queueFileName;
    return this;
  }
  
  public String getQueueFilename() {
    return queueFilename;
  }
  
  public boolean isUseDefaultActionGeneratorConfigs() {
    return useDefaultActionGeneratorConfigs;
  }
  
  public WebTestingConfig setUseDefaultActionGeneratorConfigs(boolean useDefaultActionGeneratorConfigs) {
    this.useDefaultActionGeneratorConfigs = useDefaultActionGeneratorConfigs;
    return this;
  }

  public WebTestingConfig setUseElementsCache(boolean useElementsCache) {
    this.useElementsCache = useElementsCache;
    return this;
  }
  
  public boolean isUseElementsCache() {
    return useElementsCache;
  }
  
  public int getPartitionNumber() {
    return partitionNumber;
  }
  
  public WebTestingConfig setPartitionNumber(int partitionNumber) {
    this.partitionNumber = partitionNumber;
    return this;
  }
  
  public int getNumPartitions() {
    return numPartitions;
  }
  
  public WebTestingConfig setNumPartitions(int numPartitions) {
    this.numPartitions = numPartitions;
    return this;
  }

  public boolean isCaptureScreenshots() {
    return captureScreenshots;
  }
  
  public WebTestingConfig setCaptureScreenshots(boolean captureScreenshots) {
    this.captureScreenshots = captureScreenshots;
    return this;
  }
}
