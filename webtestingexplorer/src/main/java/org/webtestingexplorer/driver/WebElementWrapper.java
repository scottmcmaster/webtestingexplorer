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
package org.webtestingexplorer.driver;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.internal.Coordinates;
import org.openqa.selenium.internal.Locatable;

import java.util.List;

/**
 * Wraps a {@link WebElement} instance to provide some caching.
 * 
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
public class WebElementWrapper implements WebElement, Locatable {

  private WebElement element;
  private String tagName;
  private String id;
  private String name;
  
  public WebElementWrapper(WebElement element) {
    this.element = element;
  }
  
  @Override
  public void click() {
    element.click();
  }

  @Override
  public void submit() {
    element.submit();
  }

  @Override
  public void sendKeys(CharSequence... keysToSend) {
    element.sendKeys(keysToSend);
  }

  @Override
  public void clear() {
    element.clear();
  }

  @Override
  public String getTagName() {
    if (tagName == null) {
      tagName = element.getTagName();
    }
    return tagName;
  }

  @Override
  public String getAttribute(String attr) {
    if ("id".equals(attr)) {
      if (id == null) {
        id = element.getAttribute("id");
      }
      return id;
    }
    
    if ("name".equals(attr)) {
      if (name == null) {
        name = element.getAttribute("name");
      }
      return name;
    }
    
    return element.getAttribute(attr);
  }

  @Override
  public boolean isSelected() {
    return element.isSelected();
  }

  @Override
  public boolean isEnabled() {
    return element.isEnabled();
  }

  @Override
  public String getText() {
    return element.getText();
  }

  @Override
  public List<WebElement> findElements(By by) {
    return element.findElements(by);
  }

  @Override
  public WebElement findElement(By by) {
    return element.findElement(by);
  }

  @Override
  public boolean isDisplayed() {
    return element.isDisplayed();
  }

  @Override
  public Point getLocation() {
    return element.getLocation();
  }

  @Override
  public Dimension getSize() {
    return element.getSize();
  }

  @Override
  public String getCssValue(String propertyName) {
    return element.getCssValue(propertyName);
  }

  @Override
  public Coordinates getCoordinates() {
    if (element instanceof Locatable) {
      return ((Locatable) element).getCoordinates();
    }
    throw new IllegalStateException("Trying to locate a non-Locatable");
  }

  @Deprecated
  @Override
  public Point getLocationOnScreenOnceScrolledIntoView() {
    if (element instanceof Locatable) {
      return ((Locatable) element).getLocationOnScreenOnceScrolledIntoView();
    }
    throw new IllegalStateException("Trying to locate a non-Locatable");
  }
}
