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
package com.google.testing.webtestingexplorer.config;

import com.google.common.collect.Lists;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Rolls together a couple of different WebElementSelectors.
 * 
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
public class CompositeWebElementSelector implements WebElementSelector {

  private final WebElementSelector first;
  private final WebElementSelector second;

  public CompositeWebElementSelector(WebElementSelector first, WebElementSelector second) {
    this.first = first;
    this.second = second;
  }
  
  @Override
  public List<WebElement> select(WebDriver driver) {
    List<WebElement> result = Lists.newArrayList();
    result.addAll(first.select(driver));
    result.addAll(second.select(driver));
    return result;
  }

}
