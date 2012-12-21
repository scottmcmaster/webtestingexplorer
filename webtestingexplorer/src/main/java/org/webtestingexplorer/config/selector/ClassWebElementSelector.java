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
package org.webtestingexplorer.config.selector;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.webtestingexplorer.config.WebElementSelector;

import java.util.List;
import java.util.logging.Logger;

/**
 * Selects {@link WebElement}s matching any of a given list of classes.
 * 
 * @author xyuan@google.com (Xun Yuan)
 */
public class ClassWebElementSelector implements WebElementSelector {
  private final static Logger LOGGER =
		      Logger.getLogger(ClassWebElementSelector.class.getName());

  private final String xpath;
  private final int maxElementsSelected;
  
  /**
   * @param isAccurate whether to do an exact match or contains() on the class names.
   * @param maxElementsSelected the maximum number of elements to return (0 means 'all').
   * @param classes a list of the classes to select from.
   */
  public ClassWebElementSelector(boolean isAccurate, int maxElementsSelected,
      String... classes) {
    this.maxElementsSelected = maxElementsSelected;
    StringBuilder xpathBuilder = new StringBuilder();
    for (String oneClass : classes) {
      if (xpathBuilder.length() > 0) {
        xpathBuilder.append(" | ");
      }
      
      xpathBuilder.append("//*[");
      
      if (isAccurate) {
        xpathBuilder.append("@class=\"");
      } else {
        xpathBuilder.append("contains(@class, \"");
      }
      
      xpathBuilder.append(oneClass);
      
      if (isAccurate) {
        xpathBuilder.append("\"");
      } else {
        xpathBuilder.append("\")");
      }
      xpathBuilder.append("]");
    }
    this.xpath = xpathBuilder.toString();
    LOGGER.info("ClassWebElementSelector xpath=" + xpath);
  }
  
  public ClassWebElementSelector(boolean isAccurate, String... classes) {
    this(isAccurate, 0, classes);
  }
  
  @Override
  public List<WebElement> select(WebDriver driver) {
    List<WebElement> elements = driver.findElements(By.xpath(xpath));
    if (maxElementsSelected > 0) {
      return elements.subList(0, maxElementsSelected);
    }
    return elements;
  }
}