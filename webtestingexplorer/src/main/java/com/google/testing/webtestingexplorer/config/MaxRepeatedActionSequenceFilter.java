// Copyright 2011 Google Inc. All Rights Reserved.

package com.google.testing.webtestingexplorer.config;

import com.google.common.collect.Maps;
import com.google.testing.webtestingexplorer.actions.Action;
import com.google.testing.webtestingexplorer.actions.ActionSequence;

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
  public boolean shouldExplore(ActionSequence actionSequence) {
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
