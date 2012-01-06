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

import static org.junit.Assert.*;

import com.google.common.collect.Lists;
import com.google.testing.webtestingexplorer.driver.WebDriverWrapper;
import com.google.testing.webtestingexplorer.state.CountOfElementsState;
import com.google.testing.webtestingexplorer.state.NullState;
import com.google.testing.webtestingexplorer.state.State;
import com.google.testing.webtestingexplorer.state.StateChecker;

import org.junit.Test;

import java.util.List;


/**
 * Tests for the {@link FinalStateCheckOracle} class.
 * 
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
public class FinalStateCheckOracleTest {

  @Test
  public void noFailures() {
    CountOfElementsState state1 = new MockCountOfElementsState(1, 1);
    List<State> states1 = Lists.<State>newArrayList(state1);    
    FinalStateCheckOracle oracle = new FinalStateCheckOracle(states1);

    List<FailureReason> result = oracle.check(null);
    assertTrue(result.isEmpty());
  }
  
  @Test
  public void twoStates() {
    CountOfElementsState state1 = new MockCountOfElementsState(1, 2);
    List<State> states1 = Lists.<State>newArrayList(state1, new NullState());    
    FinalStateCheckOracle oracle = new FinalStateCheckOracle(states1);

    List<FailureReason> result = oracle.check(null);
    assertEquals(1, result.size());
    assertEquals(
        "State check failed for state: CountOfElementsState\n   numElements: actual -- 2, expected -- 1\n",
        result.get(0).getMessage());
  }
  
  @Test
  public void oneState() {
    CountOfElementsState state1 = new MockCountOfElementsState(1, 2);
    List<State> states1 = Lists.<State>newArrayList(state1);    
    FinalStateCheckOracle oracle = new FinalStateCheckOracle(states1);

    List<FailureReason> result = oracle.check(null);
    assertEquals(1, result.size());
    assertEquals(
        "State check failed for state: CountOfElementsState\n   numElements: actual -- 2, expected -- 1\n",
        result.get(0).getMessage());
  }
  
  public static class MockCountOfElementsState extends CountOfElementsState {

    private int actualNumElements;
    
    public MockCountOfElementsState(int numElements, int actualNumElements) {
      super(numElements);
      this.actualNumElements = actualNumElements;
    }

    @Override
    public StateChecker createStateChecker() {
      return  new StateChecker() {
        @Override
        public State createState(WebDriverWrapper driver) {
          return new CountOfElementsState(actualNumElements);
        }
      };
    }
  }
}
