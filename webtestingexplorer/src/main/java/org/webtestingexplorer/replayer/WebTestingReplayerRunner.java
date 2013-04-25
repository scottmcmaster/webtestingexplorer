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
package org.webtestingexplorer.replayer;

import org.webtestingexplorer.config.OracleConfig;
import org.webtestingexplorer.config.OracleConfigFactory;
import org.webtestingexplorer.config.WebElementSelectorRegistry;
import org.webtestingexplorer.config.waitcondition.WaitConditionConfig;
import org.webtestingexplorer.config.waitcondition.WaitConditionConfigFactory;
import org.webtestingexplorer.driver.ActionSequenceRunner;
import org.webtestingexplorer.driver.ActionSequenceRunner.ActionSequenceResult;
import org.webtestingexplorer.driver.ActionSequenceRunnerConfig;
import org.webtestingexplorer.oracles.FinalStateCheckOracle;
import org.webtestingexplorer.testcase.TestCase;

/**
 * Common code for running test cases.
 * 
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
public class WebTestingReplayerRunner {

  private static final int NUM_RETRIES = 3;

  private ActionSequenceRunner runner;

  public WebTestingReplayerRunner(ActionSequenceRunner runner) throws Exception {
    this.runner = runner;
  }
  
  public ActionSequenceResult runTestCase(TestCase testCase) throws Exception {
    WebElementSelectorRegistry.setInstance(testCase.getTestCaseConfig().getSelectorRegistry());
    OracleConfig oracleConfig = createOracleConfig(testCase);
    WaitConditionConfig waitConditionConfig = extractWaitConditionConfig(testCase);    
    ActionSequenceResult result = runner.runActionSequence(new ActionSequenceRunnerConfig(
        testCase.getUrl(),
        testCase.getActionSequence(),
        oracleConfig,
        waitConditionConfig,
        null,
        NUM_RETRIES,
        true,
        false));
    
    runner.getDriver().close();
    return result;
  }

  @SuppressWarnings("unchecked")
  private WaitConditionConfig extractWaitConditionConfig(TestCase testCase)
      throws ClassNotFoundException, InstantiationException, IllegalAccessException {
    WaitConditionConfig waitConditionConfig = null;
    if (testCase.getWaitConditionConfigFactoryClassName() != null) {
      Class<? extends WaitConditionConfigFactory> waitConditionConfigFactoryClass =
          (Class<? extends WaitConditionConfigFactory>) Class.forName(testCase.getWaitConditionConfigFactoryClassName());
      WaitConditionConfigFactory waitConditionConfigFactory = waitConditionConfigFactoryClass.newInstance();
      waitConditionConfig = waitConditionConfigFactory.createWaitConditionConfig();
    }
    return waitConditionConfig;
  }

  /**
   * Creates the oracle configuration for replaying the given testcase.
   */
  @SuppressWarnings("unchecked")
  private OracleConfig createOracleConfig(TestCase testCase) throws Exception {
    OracleConfig oracleConfig;
    if (testCase.getOracleConfigFactoryClassName() != null) {
      Class<? extends OracleConfigFactory> oracleConfigFactoryClass =
          (Class<? extends OracleConfigFactory>) Class.forName(testCase.getOracleConfigFactoryClassName());
      OracleConfigFactory oracleConfigFactory = oracleConfigFactoryClass.newInstance();
      oracleConfig = oracleConfigFactory.createOracleConfig();
    } else {
      oracleConfig = new OracleConfig();
    }
    // Add a final-state checker.
    oracleConfig.addFinalOracle(new FinalStateCheckOracle(testCase.getFinalState()));
    return oracleConfig;
  }
}
