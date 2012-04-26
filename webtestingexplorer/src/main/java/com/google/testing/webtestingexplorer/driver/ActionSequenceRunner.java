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
package com.google.testing.webtestingexplorer.driver;

import com.google.testing.webtestingexplorer.actions.Action;
import com.google.testing.webtestingexplorer.actions.ActionSequence;
import com.google.testing.webtestingexplorer.config.WaitConditionConfig;
import com.google.testing.webtestingexplorer.oracles.Failure;
import com.google.testing.webtestingexplorer.oracles.FailureReason;
import com.google.testing.webtestingexplorer.oracles.Oracle;
import com.google.testing.webtestingexplorer.wait.WaitCondition;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Contains the logic for running an {@link ActionSequence}.
 * 
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
public class ActionSequenceRunner {
  
  private final static Logger LOGGER = Logger.getLogger(ActionSequenceRunner.class.getName());

  /**
   * Implement to receive a callback before each action in the sequence is
   * executed.
   */
  public interface BeforeActionCallback {
    void onBeforeAction(Action action);
  }
  
  /**
   * Includes result information about an action sequence execution.
   */
  public static class ActionSequenceResult {
    /**
     * Any failures that occurred. The list may be null or empty.
     */
    private final List<FailureReason> failures;
    
    public ActionSequenceResult(List<FailureReason> failures) {
      this.failures = failures;
    }
    
    public List<FailureReason> getFailures() {
      return failures;
    }

    /**
     * @return whether or not we have errors.
     */
    public boolean hasErrors() {
      return failures != null && !failures.isEmpty();
    }
  }
  
  private WebDriverProxy proxy;
  private WebDriverWrapper driver;
  private WebDriverFactory driverFactory;
  
  public ActionSequenceRunner(WebDriverFactory driverFactory)
      throws Exception {
    if (driverFactory.shouldUseProxy()) {
      this.proxy = new WebDriverProxy();
    }
    this.driverFactory = driverFactory;
    this.driverFactory.init();
  }
  
  public WebDriverWrapper getDriver() {
    return driver;
  }
  
  /**
   * Executes the given action sequence using the given driver.
   * @param config TODO(smcmaster):
   * @throws Exception 
   */
  public ActionSequenceResult runActionSequence(ActionSequenceRunnerConfig config)
      throws Exception {
    
    int tryNumber = 1;
    while (tryNumber <= config.getNumRetries()) {
      try {
        LOGGER.info("Try #" + tryNumber + ", at url: " + config.getUrl() + " Run action sequence: "
            + config.getActionSequence().toString());
        long waitIntervalMillis = WaitConditionConfig.DEFAULT_WAIT_INTERVAL_MILLIS;
        long waitTimeoutMillis = WaitConditionConfig.DEFAULT_WAIT_TIMEOUT_MILLIS;
        if (config.getWaitConditionConfig() != null) {
          waitIntervalMillis = config.getWaitConditionConfig().getWaitIntervalMillis();
          waitTimeoutMillis = config.getWaitConditionConfig().getWaitTimeoutMillis();
        }
        updateProxyResponseWaitTimes(waitIntervalMillis, waitTimeoutMillis);
        
        // TODO(smcmaster): We ought to manage the lifetime of the driver
        // in here instead of leaving it around to be cleaned up later.
        // But that is not entirely
        // straightforward because we need to provide the driver to callers while
        // it is still open so that they can do things like examine state.
        // Probably need to add more callbacks.
        driver = new WebDriverWrapper(driverFactory, proxy, config.getActionableWebElementSelctor(),
            config.getStatefulWebElementSelector(), waitIntervalMillis, waitTimeoutMillis,
            false);
    
        loadUrl(driver, config.getUrl(), config.getWaitConditionConfig());
        
        for (int i = 0; i < config.getActionSequence().getActions().size(); ++i) {
          Action action = config.getActionSequence().getActions().get(i);
          if (config.getBeforeActionCallback() != null) {
            config.getBeforeActionCallback().onBeforeAction(action);
          }
          performAction(driver, action, config.getWaitConditionConfig());
          
          if (config.getOracleConfig() != null) {
            // Check for failures.
            checkForFailures(config.getOracleConfig().getAfterActionOracles(), driver,
                config.getActionSequence(), action);
          }
        }
        
        List<FailureReason> failures = null;
        if (config.getOracleConfig() != null) {
          // Check for failures.
          failures = checkForFailures(config.getOracleConfig().getFinalOracles(), driver,
              config.getActionSequence(), config.getActionSequence().getLastAction());
        }
        
        return new ActionSequenceResult(failures);
      } catch (Exception e) {
        String source = driver.getDriver().getPageSource();
        LOGGER.log(Level.SEVERE, "Exception running action sequence: " + toString() +
            ", page source:\n" + source,
            e);
        try { driver.close(); } catch (Exception e2) {}
        ++tryNumber;
      }
    }
    throw new RuntimeException("Out of retries");
  }

  /**
   * Cleans up nicely.
   */
  public void shutdown() {
    if (proxy != null) {
      proxy.stop();
    }
  }

  /**
   * Pushes the wait time values into the proxy.
   */
  private void updateProxyResponseWaitTimes(long waitIntervalMillis, long waitTimeoutMillis) {
    if (proxy != null) {
      proxy.setResponseWaitIntervalMillis(waitTimeoutMillis);
      proxy.setResponseWaitTimeoutMillis(waitTimeoutMillis);
    }
  }
  
  private void loadUrl(WebDriverWrapper driver, String url,
      WaitConditionConfig waitConditionConfig) {
    List<WaitCondition> initialWaitConditions = null;
    if (waitConditionConfig != null) {
      waitConditionConfig.getInitialWaitConditions();
    }
    driver.get(url, initialWaitConditions);
  }

  /**
   * Checks the given oracles for failures and returns any it finds in a list.
   */
  private List<FailureReason> checkForFailures(List<Oracle> oracles,
      WebDriverWrapper driver,
      ActionSequence actionSequence,
      Action action) {
    List<FailureReason> failureReasons = new ArrayList<FailureReason>();
    for (Oracle oracle : oracles) {
      LOGGER.info("Checking for failures using oracle " + oracle.getClass().getSimpleName());
      failureReasons.addAll(oracle.check(driver));
    }
    if (!failureReasons.isEmpty()) {
      // Create a failure.
      Failure failure = new Failure(actionSequence, action);
      failure.addReasons(failureReasons);
      LOGGER.log(Level.INFO, "Failure detected: " + failure);
    }
    return failureReasons;
  }

  private void performAction(WebDriverWrapper driver, Action action,
      WaitConditionConfig waitConditionConfig) {
    if (proxy != null) {
      // We should reset the proxy on each action (keeping in mind that we
      // don't really know which actions will actually trigger http
      // request/responses.
      proxy.resetForRequest();
    }
    
    action.perform(driver);
    driver.invalidateElementsCache();
    if (waitConditionConfig != null) {
      driver.waitOnConditions(waitConditionConfig.getAfterActionWaitConditions());
    }
  }
}
