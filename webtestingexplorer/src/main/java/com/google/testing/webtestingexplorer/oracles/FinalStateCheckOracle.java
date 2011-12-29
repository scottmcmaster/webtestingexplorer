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
package com.google.testing.webtestingexplorer.oracles;

import com.google.testing.webtestingexplorer.driver.WebDriverWrapper;
import com.google.testing.webtestingexplorer.state.State;
import com.google.testing.webtestingexplorer.state.StateChecker;

import java.util.ArrayList;
import java.util.List;

/**
 * Checks for differences between expected and actual state at the end of a
 * testcase. In its current form, this oracle isn't very useful during the
 * exploration process.
 * 
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
public class FinalStateCheckOracle implements Oracle {

  private List<State> finalState;
  
  public FinalStateCheckOracle(List<State> finalState) {
    this.finalState = finalState;
  }

  @Override
  public List<FailureReason> check(WebDriverWrapper driver) {
    List<FailureReason> result = new ArrayList<FailureReason>();
    for (State expectedFinalState : finalState) {
      StateChecker checker = expectedFinalState.createStateChecker();
      State actualFinalState = checker.createState(driver);
      if (!actualFinalState.equals(expectedFinalState)) {
        result.add(createFailureReason(actualFinalState, expectedFinalState));      
      }
    }
    return result;
  }

  /**
   * Creates a failure message for two states presumed to not be equal.
   * 
   * @param actualFinalState the actual (observed) final state.
   * @param expectedFinalState the expected final state.
   * @return a failure reason suitable for logging as the output.
   */
  private FailureReason createFailureReason(State actualFinalState, State expectedFinalState) {
    // TODO(smcmaster): Provide an API for letting states format their differences
    // appropriately based on what they are doing.
    String message = "State check failed for state: " + actualFinalState.getClass().getSimpleName();
    return new FailureReason(message);
  }
}
