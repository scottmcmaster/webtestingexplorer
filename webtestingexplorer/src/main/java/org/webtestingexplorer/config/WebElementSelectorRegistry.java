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

import java.util.Map;

/**
 * Global registry of {@link WebElementSelector}s, including special
 * ones for stateful and actionable elements.
 * 
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
public class WebElementSelectorRegistry {

  /**
   * The registry key for the stateful {@link WebElementSelector}.
   */
  public static final String STATEFUL_ELEMENT_SELECTOR_KEY = "STATEFUL";
  
  /**
   * The registry key for the actionable {@link WebElementSelector}.
   */
  public static final String ACTIONABLE_ELEMENT_SELECTOR_KEY = "ACTIONABLE";
  
  private static final Map<String, WebElementSelector> selectorsByKey = Maps.newHashMap();
  
  /**
   * Registers a selector. If there is already one there with the same key, it is
   * replaced.
   */
  public static void register(String key, WebElementSelector selector) {
    selectorsByKey.put(key, selector);
  }
  
  /**
   * Gets a selector with the given key. Returns null if there isn't one.
   */
  public static WebElementSelector get(String key) {
    return selectorsByKey.get(key);
  }
  
  /**
   * Gets the stateful web element selector. Could be null.
   */
  public static WebElementSelector getStateful() {
    return selectorsByKey.get(STATEFUL_ELEMENT_SELECTOR_KEY);
  }
  
  /**
   * Gets the actionable web element selector. Could be null.
   */
  public static WebElementSelector getActionable() {
    return selectorsByKey.get(ACTIONABLE_ELEMENT_SELECTOR_KEY);
  }

  /**
   * Registers the stateful {@link WebElementSelector}.
   */
  public static void registerStateful(WebElementSelector selector) {
    register(STATEFUL_ELEMENT_SELECTOR_KEY, selector);
  }

  /**
   * Registers the actionable {@link WebElementSelector}.
   */
  public static void registerActionable(WebElementSelector selector) {
    register(ACTIONABLE_ELEMENT_SELECTOR_KEY, selector);
  }
}
