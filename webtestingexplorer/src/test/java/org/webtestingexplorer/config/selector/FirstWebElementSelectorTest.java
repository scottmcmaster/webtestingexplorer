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
 * Tests for the {@link FirstWebElementSelector} class.
 * 
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
public class FirstWebElementSelectorTest {

  @Before
  public void setUp() {
  }
  
  @Test
  public void nonEmpty() {
    WebElementSelector selector1;
    final WebElement element1;
    final WebElement element2;
    element1 = new FakeWebElement();
    element2 = new FakeWebElement();
    selector1 = new WebElementSelector() {
      @Override
      public List<WebElement> select(WebDriver driver) {
        return Lists.newArrayList(element1, element2);
      }
    };
    FirstWebElementSelector selector = new FirstWebElementSelector(selector1);
    List<WebElement> result = selector.select(null);
    assertEquals(1, result.size());
    assertTrue(result.contains(element1));
  }
  
  @Test
  public void empty() {
    WebElementSelector selector1;
    selector1 = new WebElementSelector() {
      @Override
      public List<WebElement> select(WebDriver driver) {
        return Lists.newArrayList();
      }
    };
    FirstWebElementSelector selector = new FirstWebElementSelector(selector1);
    List<WebElement> result = selector.select(null);
    assertTrue(result.isEmpty());
  }
}
