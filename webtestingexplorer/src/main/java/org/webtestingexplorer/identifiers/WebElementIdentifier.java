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


import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.openqa.selenium.WebElement;
import org.webtestingexplorer.driver.WebDriverWrapper;
import org.webtestingexplorer.driver.WebElementWrapper;

import java.util.List;

/**
 * To be extended by classes that know how to pull {@link WebElement}s out
 * of a driver.
 * 
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
public abstract class WebElementIdentifier {
  
  protected String frameIdentifier;
  
  public WebElementIdentifier(String frameIdentifier) {
    this.frameIdentifier = frameIdentifier;
  }
  
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
  
  public abstract WebElementWrapper findElement(WebDriverWrapper driver);
  public abstract List<WebElementWrapper> findElements(WebDriverWrapper driver);
  
  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof WebElementIdentifier)) {
      return false;
    }
    WebElementIdentifier other = (WebElementIdentifier) obj;
    return new EqualsBuilder().append(frameIdentifier, other.frameIdentifier)
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(frameIdentifier).hashCode();
  }
}