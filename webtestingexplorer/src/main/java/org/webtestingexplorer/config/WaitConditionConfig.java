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

import org.webtestingexplorer.wait.WaitCondition;

import java.util.ArrayList;
import java.util.List;

/**
 * A collection of different types of {@link WaitCondition}s.
 * 
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
public class WaitConditionConfig {

  public static final int DEFAULT_WAIT_TIMEOUT_MILLIS = 30000;
  public static final int DEFAULT_WAIT_INTERVAL_MILLIS = 1000;

  /**
   * The amount of time we wait between checking each condition.
   * Defaults to 1000ms.
   */
  private long waitIntervalMillis = DEFAULT_WAIT_INTERVAL_MILLIS;
  
  /**
   * The amount of time we wait in total for wait conditions to become true.
   * Defaults to 30000ms.
   */
  private long waitTimeoutMillis = DEFAULT_WAIT_TIMEOUT_MILLIS;
  
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
  
  public long getWaitIntervalMillis() {
    return waitIntervalMillis;
  }
  
  public WaitConditionConfig setWaitIntervalMillis(long waitIntervalMillis) {
    this.waitIntervalMillis = waitIntervalMillis;
    return this;
  }
  
  public long getWaitTimeoutMillis() {
    return waitTimeoutMillis;
  }
  
  public WaitConditionConfig setWaitTimeoutMillis(long waitTimeoutMillis) {
    this.waitTimeoutMillis = waitTimeoutMillis;
    return this;
  }
}
