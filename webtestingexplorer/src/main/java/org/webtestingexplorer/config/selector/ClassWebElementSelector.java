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

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.webtestingexplorer.config.WebElementSelector;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Selects {@link WebElement}s matching any of a given list of classes.
 * 
 * @author xyuan@google.com (Xun Yuan)
 */
public class ClassWebElementSelector implements WebElementSelector {
  private final static Logger LOGGER =
		      Logger.getLogger(ClassWebElementSelector.class.getName());

  private Set<String> classNames;
  private boolean isAccurate;
  private boolean useXpath;
  
  protected ClassWebElementSelector() {
    // For xstream.
  }

  public ClassWebElementSelector(boolean isAccurate, String... classes) {
    this(false, isAccurate, classes);
  }
  
  /**
   * @param useXpath Set to true to use xpath, false to use className selection.
   *        Must be true if isAccurate is false.
   * @param isAccurate whether to do an exact match or contains() on the class names.
   * @param classes a list of the classes to select from.
   */
  public ClassWebElementSelector(boolean useXpath, boolean isAccurate, String... classes) {
    if (!isAccurate) {
      assert useXpath : "Must use xpath with inexact classname queries";
    }
    this.useXpath = useXpath;
    this.isAccurate = isAccurate;
    classNames = Sets.newHashSet();
  }
  
  @Override
  public List<WebElement> select(WebDriver driver) {
    if (useXpath) {
      StringBuilder xpathBuilder = new StringBuilder();
      for (String oneClass : classNames) {
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
      
      String xpath = xpathBuilder.toString();
      LOGGER.info("ClassWebElementSelector xpath=" + xpath);
      return driver.findElements(By.xpath(xpath));
    } else {
      List<WebElement> elements = Lists.newArrayList();
      for (String className : classNames) {
        elements.addAll(driver.findElements(By.className(className)));
      }
      return elements;
    }
  }
}
