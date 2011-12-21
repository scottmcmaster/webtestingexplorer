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
import org.openqa.selenium.support.ui.Select;

/**
 * @author smcmaster@google.com (Scott McMaster)
 */
public class SelectAction extends Action {

  private int optionIndex;

  /**
   * Sets up an action to select an option at the given index.
   */
  public SelectAction(WebElementIdentifier identifier, int optionIndex) {
    super(identifier);
    this.optionIndex = optionIndex;
  }

  @Override
  public void perform(WebDriverWrapper driver) {
    Select element = new Select(identifier.findElement(driver));
    if (element.isMultiple()) {
      // TODO(smcmaster): We probably ought to have some special options for multi's.
      element.deselectAll();
    }
    element.selectByIndex(optionIndex);
  }
  
  @Override
  public String toString() {
    return identifier.toString() + ": Select(" + optionIndex + ")";
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof SelectAction)) {
      return false;
    }
    SelectAction other = (SelectAction) obj;
    return new EqualsBuilder().appendSuper(super.equals(obj))
        .append(optionIndex, other.optionIndex).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().appendSuper(super.hashCode()).append(optionIndex).hashCode();
  }
}
