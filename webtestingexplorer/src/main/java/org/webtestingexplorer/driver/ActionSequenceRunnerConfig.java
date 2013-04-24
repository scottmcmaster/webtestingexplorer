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
import org.webtestingexplorer.config.waitcondition.WaitConditionConfig;
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
   * Number of times we try re-running a sequence on an unhandled exception.
   * Defaults to 3.
   */
  private int numRetries = 3;

  /**
   * Whether or not to try to use cached elements in the driver wrapper.
   */
  private boolean useElementsCache;

  /**
   * Constructor.
   */
  public ActionSequenceRunnerConfig(String url, ActionSequence actionSequence,
      OracleConfig oracleConfig, WaitConditionConfig waitConditionConfig,
      BeforeActionCallback beforeActionCallback, int numRetries,
      boolean useElementsCache) {
    this.url = url;
    this.actionSequence = actionSequence;
    this.oracleConfig = oracleConfig;
    this.waitConditionConfig = waitConditionConfig;
    this.beforeActionCallback = beforeActionCallback;
    this.numRetries = numRetries;
    this.useElementsCache = useElementsCache;
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

  public boolean isUseElementsCache() {
    return useElementsCache;
  }
}