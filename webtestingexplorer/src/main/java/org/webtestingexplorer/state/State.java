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
package org.webtestingexplorer.state;

import java.util.List;

/**
 * Supports different state representations.
 * 
 * @author smcmaster@google.com (Scott McMaster)
 */
public interface State {
  /**
   * Creates the checker appropriate to this state.
   * When exploring, the state checker creates states and (potentially) saves
   * them to a test case for later replaying. When replaying, we ask the state
   * to create the appropriate checker.
   */
  StateChecker createStateChecker();
  
  /**
   * Checks for differences between this state and the given one.
   * 
   * @param other the state to diff against.
   * @return a list of differences, which may be empty if there are none.
   */
  List<StateDifference> diff(State other);
}
