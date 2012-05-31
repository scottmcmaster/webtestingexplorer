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
package org.webtestingexplorer.driver;

import org.webtestingexplorer.actions.ActionSequence;
import org.webtestingexplorer.config.OracleConfig;
import org.webtestingexplorer.config.WaitConditionConfig;
import org.webtestingexplorer.config.WebElementSelector;
import org.webtestingexplorer.driver.ActionSequenceRunner.BeforeActionCallback;

/**
 * Configuration parameters used by the {@link ActionSequenceRunner}.
 * 
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
public class ActionSequenceRunnerConfig {
  
  /**
   * The start url.
   */
  private String url;
  
  /**
   * The action sequence to run.
   */
  private ActionSequence actionSequence;
  
  /**
   * Oracle info.
   */
  private OracleConfig oracleConfig;
  
  /**
   * Wait condition info.
   */
  private WaitConditionConfig waitConditionConfig;
  
  /**
   * Called before an action is executed.
   */
  private BeforeActionCallback beforeActionCallback;

  /**
   * Optional selector that retrieves just the elements that the explorer
   * should take action on.
   */
  private WebElementSelector actionableWebElementSelctor;
  
  /**
   * Optional selector that retrieves just the elements that plan to evaluate
   * state on.
   */
  private WebElementSelector statefulWebElementSelector;

  /**
   * Number of times we try re-running a sequence on an unhandled exception.
   * Defaults to 3.
   */
  private int numRetries = 3;

  /**
   * Constructor.
   */
  public ActionSequenceRunnerConfig(String url, ActionSequence actionSequence,
      OracleConfig oracleConfig, WaitConditionConfig waitConditionConfig,
      BeforeActionCallback beforeActionCallback, WebElementSelector actionableWebElementSelctor,
      WebElementSelector statefulWebElementSelector, int numRetries) {
    this.url = url;
    this.actionSequence = actionSequence;
    this.oracleConfig = oracleConfig;
    this.waitConditionConfig = waitConditionConfig;
    this.beforeActionCallback = beforeActionCallback;
    this.actionableWebElementSelctor = actionableWebElementSelctor;
    this.statefulWebElementSelector = statefulWebElementSelector;
    this.numRetries = numRetries;
  }

  public WebElementSelector getActionableWebElementSelctor() {
    return actionableWebElementSelctor;
  }

  public WebElementSelector getStatefulWebElementSelector() {
    return statefulWebElementSelector;
  }

  public String getUrl() {
    return url;
  }

  public ActionSequence getActionSequence() {
    return actionSequence;
  }

  public OracleConfig getOracleConfig() {
    return oracleConfig;
  }

  public WaitConditionConfig getWaitConditionConfig() {
    return waitConditionConfig;
  }

  public BeforeActionCallback getBeforeActionCallback() {
    return beforeActionCallback;
  }
  
  public int getNumRetries() {
    return numRetries;
  }
}