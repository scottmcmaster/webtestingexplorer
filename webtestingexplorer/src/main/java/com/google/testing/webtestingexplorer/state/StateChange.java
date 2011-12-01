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
package com.google.testing.webtestingexplorer.state;

import java.util.List;

/**
 * Stores "before" and "after" states for comparison.
 * 
 * @author scott.d.mcmaster@gmail.com  (Scott McMaster)
 */
public class StateChange {
  private List<State> beforeState;
  private List<State> afterState;
  
  public List<State> getBeforeState() {
    return beforeState;
  }

  public void setBeforeState(List<State> beforeState) {
    this.beforeState = beforeState;
  }

  public List<State> getAfterState() {
    return afterState;
  }

  public void setAfterState(List<State> afterState) {
    this.afterState = afterState;
  }
  
  public boolean isStateChanged() {
    return !afterState.equals(beforeState);
  }
}
