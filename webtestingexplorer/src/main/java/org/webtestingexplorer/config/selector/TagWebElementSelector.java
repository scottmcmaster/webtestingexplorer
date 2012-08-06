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

import java.util.List;

/**
 * Selects {@link WebElement}s matching any of a given list of tags.
 * 
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
public class TagWebElementSelector implements WebElementSelector {

  private String xpath;
  
  public TagWebElementSelector(String... tags) {
    xpath = "";
    for (String tag : tags) {
      if (xpath.length() > 0) {
        xpath += " | ";
      }
      xpath += "//";
      xpath += tag;
    }
  }
  
  @Override
  public List<WebElement> select(WebDriver driver) {
    return driver.findElements(By.xpath(xpath));
  }
}
