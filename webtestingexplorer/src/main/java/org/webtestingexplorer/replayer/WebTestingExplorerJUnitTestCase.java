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

import static org.junit.Assert.fail;

import com.google.common.collect.Lists;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.webtestingexplorer.driver.ActionSequenceRunner;
import org.webtestingexplorer.driver.ActionSequenceRunner.ActionSequenceResult;
import org.webtestingexplorer.driver.FirefoxWebDriverFactory;
import org.webtestingexplorer.testcase.TestCase;
import org.webtestingexplorer.testcase.ReplayableTestCaseReader;

import java.io.File;
import java.util.Collection;

/**
 * Replays a WebTestingExplorer test cases as a JUnit TestCase.
 * 
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
@RunWith(Parameterized.class)
public class WebTestingExplorerJUnitTestCase {

  @Parameters
  public static Collection<Object[]> getFiles() {
    String suiteDirectory = System.getProperty("webtestingexplorersuite");
    if (suiteDirectory == null) {
      throw new RuntimeException("You must set -Dwebtestingexplorersuite on the JVM");
    }
    
    Collection<Object[]> params = Lists.newArrayList();
    File inputFile = new File(suiteDirectory);
    for (File file : inputFile.listFiles()) {
      if (".".equals(file.getName()) || "..".equals(file.getName())) {
        continue;
      }
      Object[] parameterArray = new Object[] { file };
      params.add(parameterArray);
    }    
    return params;
  }
  
  private static ActionSequenceRunner actionSequenceRunner;
  private final File inputFile;
  
  @BeforeClass
  public static void beforeClass() throws Exception {
    actionSequenceRunner = new ActionSequenceRunner(new FirefoxWebDriverFactory());
  }
  
  @AfterClass
  public static void afterClass() throws Exception {
    actionSequenceRunner.shutdown();
  }
  
  public WebTestingExplorerJUnitTestCase(File inputFile) {
    this.inputFile = inputFile;
  }
  
  @Test
  public void runTest() throws Throwable {
    ReplayableTestCaseReader reader = new ReplayableTestCaseReader();
    TestCase testCase = reader.readTestCase(inputFile.getAbsolutePath());
    WebTestingReplayerRunner runner = new WebTestingReplayerRunner(actionSequenceRunner);
    ActionSequenceResult result = runner.runTestCase(testCase);
    if (result.hasFailures()) {
      fail(result.appendFailures());
    }
  }
}
