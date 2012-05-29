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
package org.webtestingexplorer.testcase;

import org.webtestingexplorer.actions.ActionSequence;
import org.webtestingexplorer.state.State;

import java.util.List;

/**
 * Represents a test case.
 * 
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
public class TestCase {

  private String url;
  private ActionSequence actionSequence;
  private String oracleConfigFactoryClassName;
  private String waitConditionConfigFactoryClassName;
  private String actionableWebElementSelectorFactoryClassName;
  private String statefulWebElementSelectorFactoryClassName;
  private List<State> finalState;
  
  public TestCase(String url, ActionSequence actionSequence,
      List<State> finalState, String oracleConfigFactoryClassName,
      String waitConditionConfigFactoryClassName,
      String actionableWebElementSelectorFactoryClassName,
      String statefulWebElementSelectorFactoryClassName) {
    this.url = url;
    this.actionSequence = actionSequence;
    this.finalState = finalState;
    this.oracleConfigFactoryClassName = oracleConfigFactoryClassName;
    this.waitConditionConfigFactoryClassName = waitConditionConfigFactoryClassName;
    this.actionableWebElementSelectorFactoryClassName =
        actionableWebElementSelectorFactoryClassName;
    this.statefulWebElementSelectorFactoryClassName =
        statefulWebElementSelectorFactoryClassName;
  }

  public String getActionableWebElementSelectorFactoryClassName() {
    return actionableWebElementSelectorFactoryClassName;
  }

  public String getStatefulWebElementSelectorFactoryClassName() {
    return statefulWebElementSelectorFactoryClassName;
  }

  public String getUrl() {
    return url;
  }

  public ActionSequence getActionSequence() {
    return actionSequence;
  }

  public String getOracleConfigFactoryClassName() {
    return oracleConfigFactoryClassName;
  }

  public String getWaitConditionConfigFactoryClassName() {
    return waitConditionConfigFactoryClassName;
  }
  
  public List<State> getFinalState() {
    return finalState;
  }
}
