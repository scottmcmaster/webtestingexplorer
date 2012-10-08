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
package org.webtestingexplorer.oracles;

import org.webtestingexplorer.driver.WebDriverWrapper;
import org.webtestingexplorer.state.State;
import org.webtestingexplorer.state.StateChecker;
import org.webtestingexplorer.state.StateDifference;

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
  public void reset() {
  	// Nothing to do.
  }
  
  @Override
  public List<FailureReason> check(WebDriverWrapper driver) {
    List<FailureReason> result = new ArrayList<FailureReason>();
    for (State expectedFinalState : finalState) {
      StateChecker checker = expectedFinalState.createStateChecker();
      State actualFinalState = checker.createState(driver);
      List<StateDifference> diff = actualFinalState.diff(expectedFinalState);
      if (!diff.isEmpty()) {
        result.add(createFailureReason(actualFinalState.getClass().getSimpleName(), diff));      
      }
    }
    return result;
  }

  /**
   * Creates a failure message for two states presumed to not be equal.
   * 
   * @param stateClassName the name of the state that is failing.
   * @param diff the differences in state.
   * @return a failure reason suitable for logging as the output.
   */
  private FailureReason createFailureReason(String stateClassName, List<StateDifference> diff) {
    StringBuilder message = new StringBuilder("State check failed for state: ");
    message.append(stateClassName);
    message.append('\n');
    for (StateDifference difference : diff) {
      message.append("   ");
      message.append("actual -- ");
      message.append(difference.formatFirstValue());
      message.append(", expected -- ");
      message.append(difference.formatSecondValue());
      message.append('\n');
    }
    return new FailureReason(message.toString());
  }
}
