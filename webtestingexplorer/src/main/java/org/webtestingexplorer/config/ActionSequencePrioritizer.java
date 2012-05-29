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

import org.webtestingexplorer.actions.ActionSequence;

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
