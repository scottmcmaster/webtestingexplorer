package org.webtestingexplorer.config.selector;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.webtestingexplorer.config.WebElementSelector;

import java.util.List;

/**
 * Selects {@link WebElement}s matching a given xpath query.
 * 
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
public class XpathWebElementSelector implements WebElementSelector {

  private String xpath;
  
  public XpathWebElementSelector(String xpath) {
    this.xpath = xpath;
  }
  
  @Override
  public List<WebElement> select(WebDriver driver) {
    List<WebElement> elements = driver.findElements(By.xpath(xpath));
    return elements;
  }
}
