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

import org.webtestingexplorer.actions.Action;
import org.webtestingexplorer.actions.ActionSequence;
import org.webtestingexplorer.config.waitcondition.WaitConditionConfig;
import org.webtestingexplorer.oracles.Failure;
import org.webtestingexplorer.oracles.FailureReason;
import org.webtestingexplorer.oracles.Oracle;
import org.webtestingexplorer.wait.WaitCondition;

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
     * @return whether or not we have failures.
     */
    public boolean hasFailures() {
      return failures != null && !failures.isEmpty();
    }
    
    /**
     * @return all the failure messages tacked together.
     */
    public String appendFailures() {
      StringBuilder builder = new StringBuilder();
      for (FailureReason reason : failures) {
        builder.append(reason.getMessage());
        builder.append('\n');
      }
      return builder.toString();
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
    
  	// Set up the proxy per the config.
    long waitIntervalMillis = WaitConditionConfig.DEFAULT_WAIT_INTERVAL_MILLIS;
    long waitTimeoutMillis = WaitConditionConfig.DEFAULT_WAIT_TIMEOUT_MILLIS;
    if (config.getWaitConditionConfig() != null) {
      waitIntervalMillis = config.getWaitConditionConfig().getWaitIntervalMillis();
      waitTimeoutMillis = config.getWaitConditionConfig().getWaitTimeoutMillis();
    }
    updateProxyResponseWaitTimes(waitIntervalMillis, waitTimeoutMillis);

    int tryNumber = 1;
    while (tryNumber <= config.getNumRetries()) {
      try {
        LOGGER.info("Try #" + tryNumber + ", at url: " + config.getUrl() + " Run action sequence: "
            + config.getActionSequence().toString());
        
        if (config.getOracleConfig() != null) {
        	resetOracles(config.getOracleConfig().getAfterActionOracles());
        	resetOracles(config.getOracleConfig().getFinalOracles());
        }
        
        // TODO(smcmaster): We ought to manage the lifetime of the driver
        // in here instead of leaving it around to be cleaned up later.
        // But that is not entirely
        // straightforward because we need to provide the driver to callers while
        // it is still open so that they can do things like examine state.
        // Probably need to add more callbacks.
        driver = new WebDriverWrapper(driverFactory, proxy, waitIntervalMillis, waitTimeoutMillis,
            false);
    
        loadUrl(driver, config.getUrl(), config.getWaitConditionConfig());
        
        for (int i = 0; i < config.getActionSequence().getActions().size(); ++i) {
          if (config.getOracleConfig() != null) {
          	resetOracles(config.getOracleConfig().getAfterActionOracles());
          }

          Action action = config.getActionSequence().getActions().get(i);
          if (config.getBeforeActionCallback() != null) {
            config.getBeforeActionCallback().onBeforeAction(action);
          }
          try {
            performAction(driver, action, config.getWaitConditionConfig());
          } catch (Exception e) {
            throw new RuntimeException("Exception running action: " + action, e);
          }
          
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
      	// Page source is sometimes useful for debugging.
        //String source = driver.getDriver().getPageSource();
        LOGGER.log(Level.SEVERE, "Exception running action sequence: " + config.getActionSequence(), e);
        try { driver.close(); } catch (Exception e2) {}
        ++tryNumber;
      }
    }
    throw new RuntimeException("Out of retries");
  }

  /**
   * Sends the reset message to all of the given oracles.
   */
  private void resetOracles(List<Oracle> oracles) {
    for (Oracle oracle : oracles) {
      oracle.reset();
    }
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
      List<FailureReason> oracleReasons = oracle.check(driver);
      if (oracleReasons != null) {
        failureReasons.addAll(oracleReasons);
      }
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
    
    LOGGER.info("Performing action: " + action.toString());
    action.perform(driver);
    driver.invalidateElementsCache();
    if (waitConditionConfig != null) {
      driver.waitOnConditions(waitConditionConfig.getAfterActionWaitConditions());
    }
  }
}
