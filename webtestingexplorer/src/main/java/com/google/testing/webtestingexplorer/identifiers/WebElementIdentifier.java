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
package com.google.testing.webtestingexplorer.identifiers;

import com.google.testing.webtestingexplorer.driver.WebDriverWrapper;

import org.openqa.selenium.WebElement;

/**
 * To be extended by classes that know how to pull {@link WebElement}s out
 * of a driver.
 * 
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
public abstract class WebElementIdentifier {
  
  private String frameIdentifier;
  
  public String getFrameIdentifier() {
    return frameIdentifier;
  }

  public void setFrameIdentifier(String frameIdentifier) {
    this.frameIdentifier = frameIdentifier;
  }

  @Override
  public String toString() {
    String frameIdentifierDesc = (frameIdentifier == null ? "none" : frameIdentifier);
    return "frame=" + frameIdentifierDesc;
  }
  
  public abstract WebElement findElement(WebDriverWrapper driver); 
}