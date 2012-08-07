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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Lists;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.webtestingexplorer.config.WebElementSelector;
import org.webtestingexplorer.testing.FakeWebElement;

import java.util.List;

/**
 * Tests for the {@link CompositeWebElementSelector} class.
 * 
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
public class CompositeWebElementSelectorTest {

  private WebElementSelector selector1;
  private WebElementSelector selector2;
  private WebElement element1;
  private WebElement element2;
  private WebElement element3;
  
  @Before
  public void setUp() {
    element1 = new FakeWebElement();
    element2 = new FakeWebElement();
    element3 = new FakeWebElement();
    selector1 = new WebElementSelector() {
      @Override
      public List<WebElement> select(WebDriver driver) {
        return Lists.newArrayList(element1, element2);
      }
    };
    selector2 = new WebElementSelector() {
      @Override
      public List<WebElement> select(WebDriver driver) {
        return Lists.newArrayList(element1, element3);
      }
    };
  }
  
  @Test
  public void union() {
    CompositeWebElementSelector selector =
        new CompositeWebElementSelector(selector1, selector2);
    List<WebElement> result = selector.select(null);
    assertEquals(3, result.size());
    assertTrue(result.contains(element1));
    assertTrue(result.contains(element2));
    assertTrue(result.contains(element3));
  }
  
  @Test
  public void intersect() {
    CompositeWebElementSelector selector =
        new CompositeWebElementSelector(true, selector1, selector2);
    List<WebElement> result = selector.select(null);
    assertEquals(1, result.size());
    assertTrue(result.contains(element1));
  }
}
