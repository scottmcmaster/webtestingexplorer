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
package org.webtestingexplorer.replayer;

import org.webtestingexplorer.config.OracleConfig;
import org.webtestingexplorer.config.OracleConfigFactory;
import org.webtestingexplorer.config.WaitConditionConfig;
import org.webtestingexplorer.config.WaitConditionConfigFactory;
import org.webtestingexplorer.config.WebElementSelectorRegistry;
import org.webtestingexplorer.driver.ActionSequenceRunner;
import org.webtestingexplorer.driver.ActionSequenceRunnerConfig;
import org.webtestingexplorer.driver.FirefoxWebDriverFactory;
import org.webtestingexplorer.oracles.FinalStateCheckOracle;
import org.webtestingexplorer.testcase.TestCase;
import org.webtestingexplorer.testcase.TestCaseReader;

import java.io.File;
import java.util.logging.Logger;

/**
 * Replays previously-generated test cases.
 * TODO(scott): Figure out how to enable easy restoration of actionable/selectable element selectors.
 * 
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
public class WebTestingReplayer {

  private final static Logger LOGGER = Logger.getLogger(WebTestingReplayer.class.getName());

  private static final int NUM_RETRIES = 3;

  private ActionSequenceRunner runner;

  public WebTestingReplayer() throws Exception {
    this.runner = new ActionSequenceRunner(new FirefoxWebDriverFactory());
  }
  
  public void runTestCase(TestCase testCase) throws Exception {
    WebElementSelectorRegistry.setInstance(testCase.getTestCaseConfig().getSelectorRegistry());
    OracleConfig oracleConfig = createOracleConfig(testCase);
    WaitConditionConfig waitConditionConfig = extractWaitConditionConfig(testCase);    
    runner.runActionSequence(new ActionSequenceRunnerConfig(
        testCase.getUrl(),
        testCase.getActionSequence(),
        oracleConfig,
        waitConditionConfig,
        null,
        NUM_RETRIES));
    
    runner.getDriver().close();
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
  
  private void shutdown() {
    runner.shutdown();
  }

  public static void main(String[] args) throws Exception {
    String input = args[0];

    File inputFile = new File(input);
    TestCaseReader reader = new TestCaseReader();
    WebTestingReplayer replayer = new WebTestingReplayer();
    if (inputFile.isDirectory()) {
      // Run all the test cases in the directory.
      for (File file : inputFile.listFiles()) {
        if (".".equals(file.getName()) || "..".equals(file.getName())) {
          continue;
        }
        TestCase testCase = reader.readTestCase(file.getAbsolutePath());
        replayer.runTestCase(testCase);
      }
    } else {
      // Run just the specified test case.
      TestCase testCase = reader.readTestCase(inputFile.getAbsolutePath());
      replayer.runTestCase(testCase);
    }
    replayer.shutdown();
  }
}
