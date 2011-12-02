// Copyright 2011 Google Inc. All Rights Reserved.

package com.google.testing.webtestingexplorer.config;

import com.google.testing.webtestingexplorer.wait.WaitCondition;

import java.util.ArrayList;
import java.util.List;

/**
 * A collection of different types of {@link WaitCondition}s.
 * 
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
public class WaitConditionConfig {

  /**
   * A list of wait conditions that apply when first loading the url.
   */
  private List<WaitCondition> initialWaitConditions = new ArrayList<WaitCondition>();
  
  /**
   * A list of wait conditions that apply after each action is performed.
   */
  private List<WaitCondition> afterActionWaitConditions = new ArrayList<WaitCondition>();
  
  public List<WaitCondition> getInitialWaitConditions() {
    return initialWaitConditions;
  }
  
  public WaitConditionConfig addInitialWaitCondition(WaitCondition waitCondition) {
    initialWaitConditions.add(waitCondition);
    return this;
  }
  
  public List<WaitCondition> getAfterActionWaitConditions() {
    return afterActionWaitConditions;
  }
  
  public WaitConditionConfig addAfterActionWaitCondition(WaitCondition waitCondition) {
    afterActionWaitConditions.add(waitCondition);
    return this;
  }  
}
