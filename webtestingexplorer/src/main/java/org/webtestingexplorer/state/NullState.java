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

import com.google.common.collect.Lists;

import java.util.List;

/**
 * No-op state class (the default if you don't specify a factory in the config).
 * 
 * @author smcmaster@google.com (Scott McMaster)
 */
public class NullState implements State {

  /**
   * Every null state is equal to every other.
   */
  @Override
  public boolean equals(Object obj) {
    return true;
  }

  @Override
  public int hashCode() {
    return 29;
  }
  
  @Override
  public StateChecker createStateChecker() {
    return new NullStateChecker();
  }

  @Override
  public List<StateDifference> diff(State other) {
    return Lists.newArrayList();
  }
}
