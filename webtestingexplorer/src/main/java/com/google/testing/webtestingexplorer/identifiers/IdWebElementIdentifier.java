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

import java.util.List;

/**
 * @author smcmaster@google.com (Scott McMaster)
 */
public class IdWebElementIdentifier extends WebElementIdentifier {

  private String id;

  public IdWebElementIdentifier(String id) {
    this(id, null);
  }
  
  public IdWebElementIdentifier(String id, String frameIdentifier) {
    this.id = id;
    this.frameIdentifier = frameIdentifier;
  }

  @Override
  public WebElement findElement(WebDriverWrapper driver) {
    return driver.findElementInFrame(By.id(id), frameIdentifier);
  }
  
  @Override
  public List<WebElement> findElements(WebDriverWrapper driver) {
    return driver.findElementsInFrame(By.id(id), frameIdentifier);
  }

  @Override
  public String toString() {
    return super.toString() + ",id=" + id;
  }
  
  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof NameWebElementIdentifier)) {
      return false;
    }
    IdWebElementIdentifier other = (IdWebElementIdentifier) obj;
    return new EqualsBuilder().appendSuper(super.equals(obj))
        .append(id, other.id).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().appendSuper(super.hashCode()).append(id).hashCode();
  }
}
