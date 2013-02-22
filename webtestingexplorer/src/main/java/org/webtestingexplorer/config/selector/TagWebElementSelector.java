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

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.webtestingexplorer.config.WebElementSelector;

import java.util.List;
import java.util.Set;

/**
 * Selects {@link WebElement}s matching any of a given list of tags.
 * 
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
public class TagWebElementSelector implements WebElementSelector {

  private Set<String> tags;
  private boolean useXpath;
  
  protected TagWebElementSelector() {
    // For xstream.
  }
  
  public TagWebElementSelector(String... tagsToSelect) {
    this(true, tagsToSelect);
  }
  
  public TagWebElementSelector(boolean useXpath, String... tagsToSelect) {
    this.useXpath = useXpath;
    tags = Sets.newLinkedHashSet();
    for (String tag : tagsToSelect) {
      tags.add(tag);
    }
  }
  
  @Override
  public List<WebElement> select(WebDriver driver) {
    if (useXpath) {
      String xpath = "";
      for (String tag : tags) {
        if (xpath.length() > 0) {
          xpath += " | ";
        }
        xpath += "//";
        xpath += tag;
      }
      return driver.findElements(By.xpath(xpath));
    } else {
      List<WebElement> elements = Lists.newArrayList();
      for (String tag : tags) {
        elements.addAll(driver.findElements(By.tagName(tag)));
      }
      return elements;
    }
  }
}
