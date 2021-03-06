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

import org.webtestingexplorer.actions.Action;
import org.webtestingexplorer.driver.WebDriverWrapper;
import org.webtestingexplorer.identifiers.WebElementWithIdentifier;

import java.util.Set;

/**
 * Customizations to the action generation process.
 * 
 * @author smcmaster@google.com (Scott McMaster)
 */
public interface ActionGeneratorConfig {
  /** Whether or not this action generator applies to the given element. */
  boolean matches(WebDriverWrapper driver, WebElementWithIdentifier elementWithId);
  
  /** Returns the set of actions to explore for the given element. */
  Set<Action> generateActions(WebDriverWrapper driver, WebElementWithIdentifier elementWithId);
  
  /** Returns whether or not this action generator should apply to the current state of the driver. */
  boolean isActive(WebDriverWrapper driver);
}
