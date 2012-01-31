/*
Copyright 2012 Google Inc. All Rights Reserved.

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
package com.google.testing.webtestingexplorer.wait;

import com.google.testing.webtestingexplorer.driver.WebDriverWrapper;

import java.util.logging.Logger;

/**
 * {@link WaitCondition} that waits for all outstanding requests to come back.
 * 
 * @author smcmaster@google.com (Scott McMaster)
 */
public class RequestResponseWaitCondition implements WaitCondition {

  private final static Logger LOGGER = Logger.getLogger(RequestResponseWaitCondition.class.getName());
  
  /**
   * The minimum amount of time we wait to make sure requests actually get the
   * chance to go out.
   */
  private long requestDelayMillis;

  public RequestResponseWaitCondition(long requestDelayMillis) {
    this.requestDelayMillis = requestDelayMillis;
  }
  
  @Override
  public void reset() {
  }

  @Override
  public boolean canContinue(WebDriverWrapper driver) {
    if (driver.getProxy() == null) {
      LOGGER.warning("No proxy configured, not waiting");
      return true;
    }
    
    // Attempt to make sure all requests get the chance to go out.
    int requestCount = driver.getProxy().getRequestCount();
    
    // If there are no requests initially, assume the action makes no requests
    // and leave immediately.
    if (requestCount == 0) {
      return true;
    }
    
    int currentRequestCount = 0;
    do {
      currentRequestCount = requestCount;
      try {
        Thread.sleep(requestDelayMillis);
      } catch (InterruptedException useless) {}
      requestCount = driver.getProxy().getRequestCount();
    } while (requestCount != currentRequestCount);
    
    int responseCount = driver.getProxy().getResponseCount();
    return (requestCount == responseCount);
  }

  @Override
  public String getDescription() {
    return "Waiting for resposes";
  }
}
