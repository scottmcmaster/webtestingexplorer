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

import org.webtestingexplorer.driver.ActionSequenceRunner;
import org.webtestingexplorer.driver.FirefoxWebDriverFactory;
import org.webtestingexplorer.testcase.TestCase;
import org.webtestingexplorer.testcase.ReplayableTestCaseReader;

import java.io.File;

/**
 * Replays previously-generated test cases.
 * TODO(scott): Figure out how to enable easy restoration of actionable/selectable element selectors.
 * 
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
public class WebTestingReplayer {

  public static void main(String[] args) throws Exception {
    String input = args[0];

    File inputFile = new File(input);
    ReplayableTestCaseReader reader = new ReplayableTestCaseReader();
    ActionSequenceRunner actionSequenceRunner = new ActionSequenceRunner(new FirefoxWebDriverFactory());
    if (inputFile.isDirectory()) {
      // Run all the test cases in the directory.
      for (File file : inputFile.listFiles()) {
        if (".".equals(file.getName()) || "..".equals(file.getName())) {
          continue;
        }
        TestCase testCase = reader.readTestCase(file.getAbsolutePath());
        WebTestingReplayerRunner runner = new WebTestingReplayerRunner(actionSequenceRunner);
        runner.runTestCase(testCase);
      }
    } else {
      // Run just the specified test case.
      TestCase testCase = reader.readTestCase(inputFile.getAbsolutePath());
      WebTestingReplayerRunner runner = new WebTestingReplayerRunner(actionSequenceRunner);
      runner.runTestCase(testCase);
    }
    actionSequenceRunner.shutdown();
  }
}
