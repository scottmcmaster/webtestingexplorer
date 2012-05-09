// Copyright 2012 Google Inc. All Rights Reserved.

package com.google.testing.webtestingexplorer.config;

import com.google.testing.webtestingexplorer.actions.ActionSequence;

import java.util.Deque;

/**
 * Reorders the action sequence queue, presumably to explore the most interesting
 * action sequences first.
 * 
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
public interface ActionSequencePrioritizer {

  /**
   * Given the current action sequence deque, returns a new one that has
   * the "most interesting" sequences at the top of the deque.
   * Note that there is no requirement that the resulting action sequence
   * deque contains exactly the same elements as the given one. So you
   * can also use this interface for filtering out (or even adding in new)
   * action sequences.
   * 
   * @param actionSequences the action sequences to examine.
   * @return the action sequences you want to run going forward.
   */
  Deque<ActionSequence> prioritize(Deque<ActionSequence> actionSequences);
}
