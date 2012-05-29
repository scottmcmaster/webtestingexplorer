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
package org.webtestingexplorer.state;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author smcmaster@google.com (Scott McMaster)
 */
public class CountOfElementsState implements State {

  private int numElements;

  public CountOfElementsState(int numElements) {
    this.numElements = numElements;
  }
  
  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    if (!(other instanceof CountOfElementsState)) {
      return false;
    }
    return diff((CountOfElementsState) other).isEmpty();
  }

  @Override
  public StateChecker createStateChecker() {
    return new CountOfElementsStateChecker();
  }

  @Override
  public List<StateDifference> diff(State otherState) {
    if (!(otherState instanceof CountOfElementsState)) {
      throw new IllegalArgumentException("Invalid state class: " + otherState.getClass().getName());
    }
    List<StateDifference> result = Lists.newArrayList();
    CountOfElementsState other = (CountOfElementsState) otherState;
    if (numElements != other.numElements) {
      result.add(new PropertyValueStateDifference(null, "numElements", numElements, other.numElements));
    }
    return result;
  }
}
