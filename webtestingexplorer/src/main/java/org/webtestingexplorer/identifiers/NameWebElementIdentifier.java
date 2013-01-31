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
import org.openqa.selenium.WebElement;
import org.webtestingexplorer.driver.WebDriverWrapper;
import org.webtestingexplorer.driver.WebElementWrapper;

import java.util.List;

/**
 * @author smcmaster@google.com (Scott McMaster)
 */
public class NameWebElementIdentifier extends WebElementIdentifier {

  private String name;

  protected NameWebElementIdentifier() {
    // For xstream.
    super();
  }
  
  public NameWebElementIdentifier(String name) {
    this(name, null);
  }
  
  public NameWebElementIdentifier(String name, String frameIdentifier) {
    super(frameIdentifier);
    this.name = name;
  }

  @Override
  public WebElementWrapper findElement(WebDriverWrapper driver) {
    WebElement element = driver.findElementInFrame(By.name(name), frameIdentifier);
    if (element != null) {
      return new WebElementWrapper(element);
    }
    logNotFoundMessage();
    return null;
  }

  @Override
  public List<WebElementWrapper> findElements(WebDriverWrapper driver) {
    List<WebElement> elements = driver.findElementsInFrame(By.name(name), frameIdentifier);
    List<WebElementWrapper> result = Lists.newArrayList();
    for (WebElement element : elements) {
      result.add(new WebElementWrapper(element));
    }
    return result;
  }

  @Override
  public String toString() {
    return super.toString() + ",name=" + name;
  }
  
  @Override
  public boolean equals(Object obj) {
  	if (obj == this) {
  		return true;
  	}
  	if (!(obj instanceof NameWebElementIdentifier)) {
  		return false;
  	}
  	NameWebElementIdentifier other = (NameWebElementIdentifier) obj;
    return new EqualsBuilder().appendSuper(super.equals(obj))
        .append(name, other.name).isEquals();
  }
  
  @Override
  public int hashCode() {
    return new HashCodeBuilder().appendSuper(super.hashCode()).append(name).hashCode();
  }

  public String getName() {
    return name;
  }
}
