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
package com.google.testing.webtestingexplorer.config;

import com.google.testing.webtestingexplorer.actions.ActionSequence;
import com.google.testing.webtestingexplorer.state.StateChecker;
import com.google.testing.webtestingexplorer.testcase.TestCaseWriter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author smcmaster@google.com (Scott McMaster)
 */
public class WebTestingConfig {
  private String url;
  private TestCaseWriter testCaseWriter;
  private int maxLength;
  private List<StateChecker> stateCheckers = new ArrayList<StateChecker>();
  private List<ActionGeneratorConfig> actionGeneratorConfigs = new ArrayList<ActionGeneratorConfig>();
  private boolean useBackButtonAction;
  private boolean useForwardButtonAction;
  private boolean useRefreshButtonAction;
  
  /**
   * The wait condition configuration.
   */
  private WaitConditionConfig waitConditionConfig;
  
  /**
   * The oracle configuration.
   */
  private OracleConfig oracleConfig;
  
  /**
   * A list of action sequences we execute to get into the interesting
   * initial state(s) to start exploration.
   */
  private List<ActionSequence> initialActionSequences = new ArrayList<ActionSequence>();
  
  public WebTestingConfig() {
  }
  
  public WaitConditionConfig getWaitConditionConfig() {
    return waitConditionConfig;
  }
  
  public WebTestingConfig setWaitConditionConfig(WaitConditionConfig waitConditionConfig) {
    this.waitConditionConfig = waitConditionConfig;
    return this;
  }
  
  public OracleConfig getOracleConfig() {
    return oracleConfig;
  }
  
  public WebTestingConfig setOracleConfig(OracleConfig oracleConfig) {
    this.oracleConfig = oracleConfig;
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
  
  public List<ActionSequence> getInitialActionSequences() {
    return initialActionSequences;
  }
  
  public WebTestingConfig addInitialActionSequence(ActionSequence initialActionSequence) {
    initialActionSequence.setInitial();
    initialActionSequences.add(initialActionSequence);
    return this;
  }
  
  public TestCaseWriter getTestCaseWriter() {
    return testCaseWriter;
  }
  
  public WebTestingConfig setTestCaseWriter(TestCaseWriter testCaseWriter) {
    this.testCaseWriter = testCaseWriter;
    return this;
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
}
