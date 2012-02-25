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

package com.google.testing.webtestingexplorer.config;

import com.google.testing.webtestingexplorer.actions.ActionSequence;

import java.util.Deque;

/**
 * Checks to see if a given {@link ActionSequence} should be explored or not.
 * 
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
public interface ActionSequenceFilter {
  /**
   * @param actionSequence the action sequence to evaluate.
   * @param existingActionSequences all the action sequences we are currently
   *    set to explore (in case we want to de-dupe or anything like that).
   * @return true if we want to explore this sequence and beyond, false if not.
   */
  boolean shouldExplore(ActionSequence actionSequence,
      Deque<ActionSequence> existingActionSequences);
}
