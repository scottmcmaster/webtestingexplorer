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
package org.webtestingexplorer.config;

import com.google.common.collect.Sets;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Set;

/**
 * Rolls together a couple of different WebElementSelectors.
 * 
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
public class CompositeWebElementSelector implements WebElementSelector {

  private final Set<WebElementSelector> selectorSet;

  public CompositeWebElementSelector(WebElementSelector... selectors) {
    selectorSet = Sets.newHashSet();
    for (WebElementSelector selector : selectors) {
      selectorSet.add(selector);
    }
  }
  
  @Override
  public Set<WebElement> select(WebDriver driver) {
    Set<WebElement> result = Sets.newHashSet();
    for (WebElementSelector selector : selectorSet) {
      result.addAll(selector.select(driver));
    }
    return result;
  }

}
