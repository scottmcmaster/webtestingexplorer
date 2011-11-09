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
import com.google.testing.webtestingexplorer.wait.WaitCondition;

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
  
  /**
   * A list of wait conditions that apply when first loading the url.
   */
  private List<WaitCondition> initialWaitConditions = new ArrayList<WaitCondition>();
  
  /**
   * A list of wait conditions that apply after each action is performed.
   */
  private List<WaitCondition> afterActionWaitConditions = new ArrayList<WaitCondition>();
  
  /**
   * A list of action sequences we execute to get into the interesting
   * initial state(s) to start exploration.
   */
  private List<ActionSequence> initialActionSequences = new ArrayList<ActionSequence>();
  
  public WebTestingConfig() {
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
  
  public List<WaitCondition> getInitialWaitConditions() {
    return initialWaitConditions;
  }
  
  public WebTestingConfig addInitialWaitCondition(WaitCondition waitCondition) {
    initialWaitConditions.add(waitCondition);
    return this;
  }
  
  public List<WaitCondition> getAfterActionWaitConditions() {
    return afterActionWaitConditions;
  }
  
  public WebTestingConfig addAfterActionWaitCondition(WaitCondition waitCondition) {
    afterActionWaitConditions.add(waitCondition);
    return this;
  }
  
  public TestCaseWriter getTestCaseWriter() {
    return testCaseWriter;
  }
  
  public WebTestingConfig setTestCaseWriter(TestCaseWriter testCaseWriter) {
    this.testCaseWriter = testCaseWriter;
    return this;
  }
}
