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
import com.google.testing.webtestingexplorer.config.WebTestingConfig;
import com.google.testing.webtestingexplorer.oracles.Failure;
import com.google.testing.webtestingexplorer.oracles.FailureReason;
import com.google.testing.webtestingexplorer.oracles.Oracle;
import com.google.testing.webtestingexplorer.state.State;
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
  
  private WebDriverProxy proxy;
  private WebTestingConfig config;

  public ActionSequenceRunner(WebTestingConfig config) throws Exception {
    this.config = config;
    this.proxy = new WebDriverProxy();
  }
  
  public WebDriverProxy getProxy() {
    return proxy;
  }
  
  /**
   * Executes the given action sequence using the given driver.
   */
  public void runActionSequence(ActionSequence actionSequence, WebDriverWrapper driver,
      BeforeActionCallback beforeActionCallback) {
    // TODO(smcmaster): We ought to manage the lifetime of the driver
    // in here instead of passing it as a parameter. But that is not entirely
    // straightforward because we need to provide the driver to callers while
    // it is still open so that they can do things like examine state.
    // Probably need to add more callbacks.
    
    loadUrl(driver);
    
    List<State> stateBeforeLastAction = null;
    for (int i = 0; i < actionSequence.getActions().size(); ++i) {
      Action action = actionSequence.getActions().get(i);
      if (beforeActionCallback != null) {
        beforeActionCallback.onBeforeAction(action);
      }
      performAction(driver, action);
      
      // Check for failures.
      checkForFailures(config.getAfterActionOracles(), driver, actionSequence, action);
    }
    
    // Check for failures.
    checkForFailures(config.getFinalOracles(), driver, actionSequence,
        actionSequence.getLastAction());
  }

  private void loadUrl(WebDriverWrapper driver) {
    driver.get(config.getUrl(), config.getInitialWaitConditions());
  }

  /**
   * Checks the given oracles for failures.
   */
  private void checkForFailures(List<Oracle> oracles,
      WebDriverWrapper driver,
      ActionSequence actionSequence,
      Action action) {
    List<FailureReason> failureReasons = new ArrayList<FailureReason>();
    for (Oracle oracle : oracles) {
      failureReasons.addAll(oracle.check(driver));
    }
    if (!failureReasons.isEmpty()) {
      // Create a failure.
      Failure failure = new Failure(actionSequence, action);
      failure.addReasons(failureReasons);
      LOGGER.log(Level.INFO, "Failure detected: " + failure);
    }
  }

  private void performAction(WebDriverWrapper driver, Action action) {
    // We should reset the proxy on each action (keeping in mind that we
    // don't really know which actions will actually trigger http
    // request/responses.
    proxy.resetForRequest();
    
    action.perform(driver);
    waitOnConditions(config.getAfterActionWaitConditions(), driver);
  }

  /**
   * Waits for the given list of conditions to be true before returning.
   * TODO(smcmaster): Make the wait between checks configurable, and add a timeout.
   */
  private void waitOnConditions(List<WaitCondition> waitConditions, WebDriverWrapper driver) {
    for (WaitCondition waitCondition : waitConditions) {
      waitCondition.reset();
    }
    while (true) {
      boolean allCanContinue = true;
      String conditionDescription = "";
      for (WaitCondition waitCondition : waitConditions) {
        if (!waitCondition.canContinue(driver)) {
          allCanContinue = false;
          conditionDescription = waitCondition.getDescription();
          break;
        }
      }
      if (allCanContinue) {
        break;
      }
      LOGGER.log(Level.INFO, "Waiting for " + conditionDescription);
      try {
        Thread.sleep(1000);
      } catch (InterruptedException useless) {
      }
    }
  }
}
