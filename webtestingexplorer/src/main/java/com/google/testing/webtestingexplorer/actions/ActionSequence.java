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
package com.google.testing.webtestingexplorer.actions;

import java.util.ArrayList;
import java.util.List;

/**
 * @author smcmaster@google.com (Scott McMaster)
 */
public class ActionSequence {
  private List<Action> actions = new ArrayList<Action>();
  
  public ActionSequence(Action action) {
    actions.add(action);
  }

  public ActionSequence(ActionSequence actionSequence) {
    actions.addAll(actionSequence.getActions());
  }

  public ActionSequence() {
  }

  public void addAction(Action action) {
    actions.add(action);
  }

  public List<Action> getActions() {
    return actions;
  } 
  
  /**
   * Gets the length of the action sequence, which excludes any initial
   * actions.
   */
  public int getLength() {
    int length = 0;
    for (Action action : actions) {
      if (!action.isInitial()) {
        ++length;
      }
    }
    return length;
  }
  
  @Override
  public String toString() {
    String result = "";
    for (Action action : actions) {
      result += action.toString();
      result += ",";
    }
    return result;
  }

  /**
   * Makes this an "initial" action sequence by marking all child Actions
   * as initial.
   */
  public void setInitial() {
    for (Action action : actions) {
      action.setInitial(true);
    }
  }

  public Action getLastAction() {
    return actions.get(actions.size() - 1);
  }
}
