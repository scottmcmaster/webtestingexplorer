package org.webtestingexplorer.config.selector;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.webtestingexplorer.config.WebElementSelector;

import java.util.List;

/**
 * Selects {@link WebElement}s matching a given css selector.
 * 
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
public class CssWebElementSelector implements WebElementSelector {

  private String css;
  
  protected CssWebElementSelector() {
    // For xstream.
  }

  public CssWebElementSelector(String css) {
    this.css = css;
  }
  
  @Override
  public List<WebElement> select(WebDriver driver) {
    List<WebElement> elements = driver.findElements(By.cssSelector(css));
    return elements;
  }
}
