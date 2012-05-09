// Copyright 2012 Google Inc. All Rights Reserved.

package com.google.testing.webtestingexplorer.config;

import com.google.common.collect.Lists;
import com.google.testing.webtestingexplorer.actions.ActionSequence;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;

/**
 * {@link ActionSequencePrioritizer} that runs the shortest action sequences
 * first.
 * 
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
public class ShortestActionSequencePrioritizer implements ActionSequencePrioritizer {

  @Override
  public Deque<ActionSequence> prioritize(Deque<ActionSequence> actionSequences) {
    List<ActionSequence> allSequences = Lists.newArrayList(actionSequences);
    Collections.sort(allSequences, new Comparator<ActionSequence>() {
      @Override
      public int compare(ActionSequence first, ActionSequence second) {
        return first.getLength() - second.getLength();
      }});
    return new ArrayDeque<ActionSequence>(allSequences);
  }
}
