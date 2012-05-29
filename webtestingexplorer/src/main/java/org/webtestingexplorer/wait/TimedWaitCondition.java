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
package org.webtestingexplorer.wait;

import org.webtestingexplorer.driver.WebDriverWrapper;

/**
 * {@link WaitCondition} that waits for a given amount of time.
 * 
 * @author smcmaster@google.com (Scott McMaster)
 */
public class TimedWaitCondition implements WaitCondition {

  private final long durationInMillis;
  private long startTime;

  public TimedWaitCondition(long durationInMillis) {
    this.durationInMillis = durationInMillis;
  }
  
  @Override
  public void reset() {
    startTime = System.currentTimeMillis();
  }

  @Override
  public boolean canContinue(WebDriverWrapper driver) {
    return (System.currentTimeMillis() - startTime > durationInMillis);
  }

  @Override
  public String getDescription() {
    return "Waiting for " + durationInMillis + " milliseconds";
  }

}
