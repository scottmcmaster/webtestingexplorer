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

package com.google.testing.webtestingexplorer.config;

import com.google.testing.webtestingexplorer.identifiers.WebElementWithIdentifier;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Action generator config picks out elements based on an xpath expression.
 * 
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
public abstract class XpathActionGeneratorConfig implements ActionGeneratorConfig {

  private final String xpath;
  
  /**
   * Creates the action generator configuration given the xpath expression
   * to evaluate when matching elements.
   * Note that it will automatically be prepended with "//" if it is not
   * already there.
   */
  public XpathActionGeneratorConfig(String xpath) {
    this.xpath = xpath;
  }
  
  /**
   * This works by looking at all the elements in the document matching
   * the xpath expression and comparing them to the one given.
   */
  @Override
  public boolean matches(WebElementWithIdentifier elementWithId) {
    List<WebElement> matchingElements = elementWithId.getElement().findElements(By.xpath(xpath));
    for (WebElement matchingElement : matchingElements) {
      // This doesn't work -- it seems that a different object reference is returned
      // by the findElementsBy call than what we got before.
      if (matchingElement == elementWithId.getElement()) {
        return true;
      }
    }
    return false;
  }
}
