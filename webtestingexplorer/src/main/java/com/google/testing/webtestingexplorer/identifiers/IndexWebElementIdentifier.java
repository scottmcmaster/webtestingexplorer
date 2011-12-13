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

import java.util.List;

import com.google.testing.webtestingexplorer.driver.WebDriverWrapper;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

/**
 * @author smcmaster@google.com (Scott McMaster)
 *
 */
public class IndexWebElementIdentifier extends WebElementIdentifier {

  private int index;

  public IndexWebElementIdentifier(int index) {
    this.index = index;
  }
  
  @Override
  public WebElement findElement(WebDriverWrapper driver) {
    List<WebElement> allElements = driver.getAllElements();
    if (index < allElements.size()) {
      return driver.getAllElements().get(index);
    } else {
      throw new NoSuchElementException("Index out of bound!");
    }
  }

  @Override
  public String toString() {
    return  super.toString() + ",index=" + index;
  }
  
  @Override
  public boolean equals(Object obj) {
  	if (obj == this) {
  		return true;
  	}
  	if (!(obj instanceof IndexWebElementIdentifier)) {
  		return false;
  	}
  	IndexWebElementIdentifier other = (IndexWebElementIdentifier) obj;
    return new EqualsBuilder().append(index, other.index).isEquals();
  }
  
  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(index).hashCode();
  }
}
