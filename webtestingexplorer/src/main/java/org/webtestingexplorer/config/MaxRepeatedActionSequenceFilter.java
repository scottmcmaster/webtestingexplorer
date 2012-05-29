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

package org.webtestingexplorer.config;

import com.google.common.collect.Maps;

import org.webtestingexplorer.actions.Action;
import org.webtestingexplorer.actions.ActionSequence;

import java.util.Deque;
import java.util.Map;

/**
 * Filters out action sequences with too many repeated actions.
 * 
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
public class MaxRepeatedActionSequenceFilter implements ActionSequenceFilter {

  private final int maxActionRepeat;
  
  public MaxRepeatedActionSequenceFilter(int maxActionRepeat) {
    this.maxActionRepeat = maxActionRepeat;
  }
  
  @Override
  public boolean shouldExplore(ActionSequence actionSequence,
      Deque<ActionSequence> existingActionSequences) {
    Map<Action, Integer> repeats = Maps.newHashMap();
    for (Action action : actionSequence.getActions()) {
      if (!repeats.containsKey(action)) {
        repeats.put(action, 1);
      } else {
        repeats.put(action, repeats.get(action) + 1);
      }
    }
    
    for (Map.Entry<Action, Integer> entry : repeats.entrySet()) {
      if (entry.getValue() > maxActionRepeat) {
        return false;
      }
    }
    return true;
  }
}
