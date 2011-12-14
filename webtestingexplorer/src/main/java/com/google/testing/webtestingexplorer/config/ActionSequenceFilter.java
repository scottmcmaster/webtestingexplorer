// Copyright 2011 Google Inc. All Rights Reserved.

package com.google.testing.webtestingexplorer.config;

import com.google.testing.webtestingexplorer.actions.ActionSequence;

/**
 * Checks to see if a given {@link ActionSequence} should be explored or not.
 * 
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
public interface ActionSequenceFilter {
  /**
   * @return true if we want to explore this sequence and beyond, false if not.
   */
  boolean shouldExplore(ActionSequence actionSequence);
}
