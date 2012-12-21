package org.webtestingexplorer.config.selector;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.webtestingexplorer.config.WebElementSelector;

import java.util.List;

/**
 * {@link WebElementSelector} that wraps another selector and allows you to
 * remove elements you don't want to expose to the caller.
 * 
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
public abstract class FilterWebElementSelector implements WebElementSelector {

  private final WebElementSelector selector;
  
  public FilterWebElementSelector(WebElementSelector selector) {
    this.selector = selector;
  }
  
  @Override
  public List<WebElement> select(WebDriver driver) {
    List<WebElement> elements = selector.select(driver);
    return filter(elements);
  }
  
  /**
   * Filters the selected {@link WebElement}s to just the ones to expose.
   * 
   * @param elements the orignal list of selected elements.
   * @return a new list which has been filtered per custom logic in this method.
   */
  public abstract List<WebElement> filter(List<WebElement> elements);
}
