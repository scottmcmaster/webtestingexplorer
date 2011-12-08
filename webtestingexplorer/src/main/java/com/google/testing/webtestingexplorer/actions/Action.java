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
package com.google.testing.webtestingexplorer.actions;

import com.google.testing.webtestingexplorer.driver.WebDriverWrapper;
import com.google.testing.webtestingexplorer.identifiers.WebElementIdentifier;

/**
 * Represents an action that we can take on the page. This action may be
 * associated with a given element on the page, for which we keep a
 * {@link WebElementIdentifier}.
 * 
 * @author smcmaster@google.com (Scott McMaster)
 */
public abstract class Action {
  protected WebElementIdentifier identifier;
  
  /** Whether this action is part of test case setup */
  private boolean initial;

  public boolean isInitial() {
    return initial;
  }

  public void setInitial(boolean initial) {
    this.initial = initial;
  }

  public Action() {  
  }
  
  public Action(WebElementIdentifier identifier) {
    this.identifier = identifier;
  }
  
  public abstract void perform(WebDriverWrapper driver);
  
  WebElementIdentifier getIdentifier() {
    return identifier;
  }
}
