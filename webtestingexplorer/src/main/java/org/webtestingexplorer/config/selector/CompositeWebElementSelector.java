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

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.webtestingexplorer.config.WebElementSelector;

import java.util.List;
import java.util.Set;

/**
 * Rolls together a couple of different WebElementSelectors and returns either
 * the union or intersection of what they select.
 * 
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
public class CompositeWebElementSelector implements WebElementSelector {

  private final List<WebElementSelector> selectorList;
  private final boolean intersect;
  
  /**
   * Creates a selector that returns the union of elements selected by the
   * given list of selectors.
   */
  public CompositeWebElementSelector(WebElementSelector... selectors) {
    this(false, selectors);
  }

  /**
   * Creates a selector that returns either a union or intersection (depending
   * on the boolean parameter) of the elements returned by the given selectors.
   * 
   * @param intersect true to intersect, false to union.
   * @param selectors the selectors to evaluate.
   */
  public CompositeWebElementSelector(boolean intersect, WebElementSelector... selectors) {
    this.intersect = intersect;
    selectorList = Lists.newArrayList();
    for (WebElementSelector selector : selectors) {
      selectorList.add(selector);
    }
  }
  
  /**
   * This method uses a set internally to guard against duplicate elements
   * pulled in by the multiple selectors.
   */
  @Override
  public List<WebElement> select(WebDriver driver) {
    Set<WebElement> result = Sets.newLinkedHashSet();
    if (intersect && !selectorList.isEmpty()) {
      result.addAll(selectorList.get(0).select(driver));
      for (WebElementSelector selector : selectorList) {
        result.retainAll(selector.select(driver));
      }
    } else {
      for (WebElementSelector selector : selectorList) {
        result.addAll(selector.select(driver));
      }
    }
    return Lists.newArrayList(result);
  }
}
