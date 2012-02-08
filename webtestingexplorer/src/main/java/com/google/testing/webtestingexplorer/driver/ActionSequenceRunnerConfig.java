// Copyright 2012 Google Inc. All Rights Reserved.

package com.google.testing.webtestingexplorer.driver;

import com.google.testing.webtestingexplorer.actions.ActionSequence;
import com.google.testing.webtestingexplorer.config.OracleConfig;
import com.google.testing.webtestingexplorer.config.WaitConditionConfig;
import com.google.testing.webtestingexplorer.driver.ActionSequenceRunner.BeforeActionCallback;

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
   * Constructor.
   */
  public ActionSequenceRunnerConfig(String url, ActionSequence actionSequence,
      OracleConfig oracleConfig, WaitConditionConfig waitConditionConfig,
      BeforeActionCallback beforeActionCallback) {
    this.url = url;
    this.actionSequence = actionSequence;
    this.oracleConfig = oracleConfig;
    this.waitConditionConfig = waitConditionConfig;
    this.beforeActionCallback = beforeActionCallback;
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
}