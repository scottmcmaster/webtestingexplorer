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
package org.webtestingexplorer.identifiers;

import com.google.common.collect.Lists;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.webtestingexplorer.driver.WebDriverWrapper;
import org.webtestingexplorer.driver.WebElementWrapper;
import org.webtestingexplorer.javascript.JavaScriptUtil;

import java.util.List;
import java.util.logging.Logger;

/**
 * @author xyuan@google.com (Xun Yuan)
 */
public class XpathWebElementIdentifier extends WebElementIdentifier {

  private final static Logger LOGGER = Logger.getLogger(XpathWebElementIdentifier.class.getName());

  private String xpath;

  protected XpathWebElementIdentifier() {
    // For xstream.
    super();
  }
  
  public XpathWebElementIdentifier(String xpath) {
    this(xpath, null);
  }
  
  public XpathWebElementIdentifier(WebDriver driver, WebElement element) {
    super(null);
    StringBuilder jsString = new StringBuilder();
    jsString.append(JavaScriptUtil.getJavaScriptFromFile("/getXpathFromWebElement.js"));

    // Execute js
    JavascriptExecutor js = (JavascriptExecutor) driver;
    xpath = (String) js.executeScript(jsString.toString(), element);
  }
  
  public XpathWebElementIdentifier(String xpath, String frameIdentifier) {
    super(frameIdentifier);
    this.xpath = xpath;
  }
  
  @Override
  public WebElementWrapper findElement(WebDriverWrapper driver) {
    WebElement element = driver.findElementInFrame(By.xpath(xpath), frameIdentifier);
    if (element !=  null) {
      return new WebElementWrapper(element);
    }
    LOGGER.warning("Failed to find " + toString());
    return null;
  }

  @Override
  public List<WebElementWrapper> findElements(WebDriverWrapper driver) {
    List<WebElement> elements = driver.findElementsInFrame(By.xpath(xpath), frameIdentifier);
    List<WebElementWrapper> result = Lists.newArrayList();
    for (WebElement element : elements) {
      result.add(new WebElementWrapper(element));
    }
    return result;
  }

  @Override
  public String toString() {
    return "xpath=" + xpath;
  }
  
  @Override
  public boolean equals(Object obj) {
  	if (obj == this) {
  		return true;
  	}
  	if (!(obj instanceof XpathWebElementIdentifier)) {
  		return false;
  	}
  	XpathWebElementIdentifier other = (XpathWebElementIdentifier) obj;
    return new EqualsBuilder().appendSuper(super.equals(obj))
        .append(xpath, other.xpath).isEquals();
  }
  
  @Override
  public int hashCode() {
    return new HashCodeBuilder().appendSuper(super.hashCode()).append(xpath).hashCode();
  }

  public String getXpath() {
    return xpath;
  }
}
