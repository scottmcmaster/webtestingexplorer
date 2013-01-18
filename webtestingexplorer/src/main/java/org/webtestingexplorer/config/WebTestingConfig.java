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

import org.webtestingexplorer.actions.ActionSequence;
import org.webtestingexplorer.config.waitcondition.WaitConditionConfig;
import org.webtestingexplorer.config.waitcondition.WaitConditionConfigFactory;
import org.webtestingexplorer.driver.FirefoxWebDriverFactory;
import org.webtestingexplorer.driver.WebDriverFactory;
import org.webtestingexplorer.state.StateChecker;
import org.webtestingexplorer.testcase.TestCaseWriter;

import java.util.List;

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
  private List<EquivalentWebElementsSet> equivalentWebElementSets = Lists.newArrayList();
  private WebDriverFactory driverFactory = new FirefoxWebDriverFactory();
  private ActionSequencePrioritizer actionSequencePrioritizer;
  private int numRetries = 3;
  private String queueFilename;
  
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
  
  public List<EquivalentWebElementsSet> getEquivalentWebElementSets() {
    return equivalentWebElementSets;
  }
  
  public WebTestingConfig addEquivalentWebElementSet(EquivalentWebElementsSet equivalentElements) {
    equivalentWebElementSets.add(equivalentElements);
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
}
