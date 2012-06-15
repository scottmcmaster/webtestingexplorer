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

package org.webtestingexplorer.testing;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * A fake {@link WebElement} implementation for unit testing.
 * 
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
public class FakeWebElement implements WebElement {

  @Override
  public void clear() {
  }

  @Override
  public void click() {
  }

  @Override
  public WebElement findElement(By arg0) {
    return null;
  }

  @Override
  public List<WebElement> findElements(By arg0) {
    return null;
  }

  @Override
  public String getAttribute(String arg0) {
    return null;
  }

  @Override
  public String getCssValue(String arg0) {
    return null;
  }

  @Override
  public Point getLocation() {
    return null;
  }

  @Override
  public Dimension getSize() {
    return null;
  }

  @Override
  public String getTagName() {
    return null;
  }

  @Override
  public String getText() {
    return null;
  }

  @Override
  public boolean isDisplayed() {
    return false;
  }

  @Override
  public boolean isEnabled() {
    return false;
  }

  @Override
  public boolean isSelected() {
    return false;
  }

  @Override
  public void sendKeys(CharSequence... arg0) {
  }

  @Override
  public void submit() {
  }
}
