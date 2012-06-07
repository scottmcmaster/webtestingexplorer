/*
Copyright 2012 Google Inc. All Rights Reserved.

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

import org.webtestingexplorer.state.StateChecker;

import java.util.Map;

/**
 * Global registry of {@link StateChecker}s.
 * 
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
public class StateCheckerRegistry {

  private static final Map<String, StateChecker> stateCheckersByKey = Maps.newHashMap();
  
  /**
   * Registers a state checker. If there is already one there with the same key, it is
   * replaced.
   */
  public static void register(String key, StateChecker selector) {
    stateCheckersByKey.put(key, selector);
  }
  
  /**
   * Gets a state checker with the given key. Returns null if there isn't one.
   */
  public static StateChecker get(String key) {
    return stateCheckersByKey.get(key);
  }
}
