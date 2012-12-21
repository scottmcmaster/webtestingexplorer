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

  private String xpath;
  
  public ClassWebElementSelector(boolean isAccurate, String... classes) {
    xpath = "";
    for (String oneClass : classes) {
      if (xpath.length() > 0) {
        xpath += " | ";
      }
      
      xpath += "//*[";
      
      if (isAccurate) {
    	  xpath +="@class=\"";
      } else {
    	  xpath +="contains(@class, \"";
      }
      
      xpath += oneClass;
      
      if (isAccurate) {
    	  xpath += "\"";
      } else {
    	  xpath += "\")";
      }
      xpath += "]";
    }
    LOGGER.info("ClassWebElementSelector xpath=" + xpath); 
  }
  
  @Override
  public List<WebElement> select(WebDriver driver) {
    return driver.findElements(By.xpath(xpath));
  }
}
