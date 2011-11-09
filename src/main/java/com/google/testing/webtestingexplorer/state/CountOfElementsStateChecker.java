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
package com.google.testing.webtestingexplorer.state;

import com.google.testing.webtestingexplorer.actions.Action;
import com.google.testing.webtestingexplorer.driver.WebDriverWrapper;

/**
 * Checks the state by simply calculating the number of elements we find
 * on the page.
 * 
 * @author smcmaster@google.com (Scott McMaster)
 */
public class CountOfElementsStateChecker implements StateChecker {

  @Override
  public State createState(WebDriverWrapper driver, Action lastAction) {
    int numElements = driver.getAllElements().size();
    return new CountOfElementsState(numElements);
  }

}
