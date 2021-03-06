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

package org.webtestingexplorer.config.prioritizer;

import com.google.common.collect.Lists;

import org.webtestingexplorer.actions.ActionSequence;
import org.webtestingexplorer.actions.ActionSequenceQueue;
import org.webtestingexplorer.config.ActionSequencePrioritizer;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * {@link ActionSequencePrioritizer} that runs the shortest action sequences
 * first.
 * 
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
public class ShortestActionSequencePrioritizer implements ActionSequencePrioritizer {

  @Override
  public ActionSequenceQueue prioritize(ActionSequenceQueue actionSequences) {
    List<ActionSequence> allSequences = Lists.newArrayList(actionSequences);
    Collections.sort(allSequences, new Comparator<ActionSequence>() {
      @Override
      public int compare(ActionSequence first, ActionSequence second) {
        return first.getLength() - second.getLength();
      }});
    return new ActionSequenceQueue(allSequences);
  }
}
