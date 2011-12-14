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

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.openqa.selenium.WebElement;

/**
 * Sends keys to the specified element.
 * 
 * @author smcmaster@google.com (Scott McMaster)
 */
public class SetTextAction extends Action {

  private String keysToSend;

  public SetTextAction(WebElementIdentifier identifier, String keysToSend) {
    super(identifier);
    this.keysToSend = keysToSend;
  }
  
  @Override
  public void perform(WebDriverWrapper driver) {
    super.perform(driver);
    WebElement element = identifier.findElement(driver);
    element.clear();
    element.sendKeys(keysToSend);
  }
  
  @Override
  public String toString() {
    return identifier.toString() + ": SetText(" + keysToSend + ")";
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof SetTextAction)) {
      return false;
    }
    SetTextAction other = (SetTextAction) obj;
    return new EqualsBuilder().appendSuper(super.equals(obj))
        .append(keysToSend, other.keysToSend).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().appendSuper(super.hashCode()).append(keysToSend).hashCode();
  }
}
