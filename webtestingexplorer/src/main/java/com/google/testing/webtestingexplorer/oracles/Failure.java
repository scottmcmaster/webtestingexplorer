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

import com.google.testing.webtestingexplorer.actions.Action;
import com.google.testing.webtestingexplorer.actions.ActionSequence;

import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates all the reasons detected for failure of an
 * {@link ActionSequence}. Contains one or more {@link FailureReason}s.
 * 
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
public class Failure {
  private final List<FailureReason> reasons;
  
  /**
   * The action sequence that failed.
   */
  private final ActionSequence actionSequence;

  /**
   * The last action that ran prior to the failure being detected.
   */
  private final Action lastAction;
  
  public Failure(ActionSequence actionSequence, Action lastAction) {
    this.actionSequence = actionSequence;
    this.lastAction = lastAction;
    reasons = new ArrayList<FailureReason>();
  }
  
  public void addReasons(List<FailureReason> newReasons) {
    reasons.addAll(newReasons);
  }
  
  public ActionSequence getActionSequence() {
    return actionSequence;
  }
  
  public Action getLastAction() {
    return lastAction;
  }
  
  @Override
  public String toString() {
    String result = "Failure Details:";
    for (FailureReason reason : reasons) {
      result += ("\n" + reason);
    }
    result += ("\nActionSequence: " + actionSequence);
    result += ("\nLastAction: " + lastAction);
    return result;
  }
}
