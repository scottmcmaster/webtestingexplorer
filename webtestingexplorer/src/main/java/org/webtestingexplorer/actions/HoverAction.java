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
package org.webtestingexplorer.actions;


import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.webtestingexplorer.driver.WebDriverWrapper;
import org.webtestingexplorer.identifiers.WebElementIdentifier;

/**
 * An action to hover over the identified element.
 * 
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
public class HoverAction extends Action {

  /** How long we wait after a hover, defaults to zero */
  private long hoverDelayMillis;
  
  protected HoverAction() {
    // For xstream.
  }
  
  public HoverAction(WebElementIdentifier identifier) {
    this(identifier, 0L);
  }
  
  public HoverAction(WebElementIdentifier identifier, long hoverDelay) {
    super(identifier);
    this.hoverDelayMillis = hoverDelay;
  }
  
  @Override
  public void perform(WebDriverWrapper driver) {
    WebElement element = identifier.findElement(driver);
    Actions builder = new Actions(driver.getDriver()); 
    Actions hoverAction = builder.moveToElement(element);
    hoverAction.perform();
    try {
      Thread.sleep(hoverDelayMillis);
    } catch (InterruptedException e) {
    }
  }
  
  @Override
  public String toString() {
    return identifier.toString() + ": Hover()";
  }
  
  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof HoverAction)) {
      return false;
    }
    HoverAction other = (HoverAction) obj;
    return new EqualsBuilder().appendSuper(super.equals(obj))
        .append(hoverDelayMillis, other.hoverDelayMillis).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().appendSuper(super.hashCode()).append(hoverDelayMillis).hashCode();
  }
}
