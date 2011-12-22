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

package com.google.testing.webtestingexplorer.identifiers;

import com.google.testing.webtestingexplorer.driver.WebDriverWrapper;

import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;

/**
 * Helper class that pairs a {@link WebElement} with its
 * {@link WebElementIdentifier}. It should be noted that identifiers can be
 * somewhat transient in nature, i.e. only "good" in a certain state
 * of the application. So probably you only want to use this like it's
 * being used now, e.g. during the action generation process.
 * 
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
public class WebElementWithIdentifier {

  private WebElement element;
  private final WebElementIdentifier identifier;
  
  public WebElementWithIdentifier(WebElement element,
      WebElementIdentifier identifier) {
    this.element = element;
    this.identifier = identifier;
  }

  public WebElement getElement() {
    return element;
  }

  public WebElementIdentifier getIdentifier() {
    return identifier;
  }
  
  /**
   * Get the element, trying to do a refresh from the driver if the element is stale.
   */
  public WebElement safeGetElement(WebDriverWrapper driver) {
    try {
      element.getTagName();
    } catch (StaleElementReferenceException e) {
      // Try to refresh from the identifier.
      element = identifier.findElement(driver);
    }
    return element;
  }
}
