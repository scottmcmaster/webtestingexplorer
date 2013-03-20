package org.webtestingexplorer.config.selector;

import com.google.common.collect.Lists;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.webtestingexplorer.config.WebElementSelector;

import java.util.List;

/**
 * Selects just the first element (if any) from the WebElementSelector it's wrapping.
 * 
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
public class FirstWebElementSelector implements WebElementSelector {

  private WebElementSelector selector;

  protected FirstWebElementSelector() {
    // For xstream.
  }

  public FirstWebElementSelector(WebElementSelector selector) {
    this.selector = selector;
  }

  @Override
  public List<WebElement> select(WebDriver driver) {
    List<WebElement> elements = selector.select(driver);
    if (elements.isEmpty()) {
      return Lists.newArrayList();
    }
    return Lists.newArrayList(elements.get(0));
  }
}
