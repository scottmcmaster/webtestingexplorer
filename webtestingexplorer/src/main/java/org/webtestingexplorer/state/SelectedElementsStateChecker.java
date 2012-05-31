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

import org.webtestingexplorer.config.WebElementSelector;
import org.webtestingexplorer.driver.WebDriverWrapper;

/**
 * State checker that compares the existence of elements selected by the supplied
 * {@link WebElementSelector}. Uses their identifiers as a proxy. Note that this
 * is heuristic in the sense that it's possible for a before state and an after
 * state to have exactly the same set of identifiers BUT the underlying elements
 * have changed (particularly if we are dealing with a lot of index-identified
 * elements).
 * 
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
public class SelectedElementsStateChecker implements StateChecker {

  private final WebElementSelector selector;
  
  public SelectedElementsStateChecker(WebElementSelector selector) {
    this.selector = selector;
  }
  
  @Override
  public State createState(WebDriverWrapper driver) {
    return new SelectedElementsState(driver.getElementsForSelector(selector),
        selector.getClass().getName());
  }

}
