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

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * @author xyuan@google.com (Xun Yuan)
 */
public class XpathWebElementIdentifier extends WebElementIdentifier {

  private String xpath;

  public XpathWebElementIdentifier(String xpath) {
    this.xpath = xpath;
  }
  
  @Override
  public WebElement findElement(WebDriverWrapper driver) {
    return driver.getDriver().findElement(By.xpath(xpath));
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
}