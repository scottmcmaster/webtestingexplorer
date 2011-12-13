// Copyright 2011 Google Inc. All Rights Reserved.

package com.google.testing.webtestingexplorer.identifiers;

import org.openqa.selenium.WebElement;

/**
 * Helper class that pairs a {@link WebElement} with its
 * {@link WebElementIdentifier}. It should be noted that identifiers can be
 * somewhat transient in nature, i.e. only "good" in a certain state
 * of the application. So probably you only want to use this like it's
 * being used now, e.g. during the action generation process.
 * 
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
public class WebElementWithIdentifier {

  private final WebElement element;
  private final WebElementIdentifier identifier;
  
  public WebElementWithIdentifier(WebElement element,
      WebElementIdentifier identifier) {
    this.element = element;
    this.identifier = identifier;
  }

  public WebElement getElement() {
    return element;
  }

  public WebElementIdentifier getIdentifier() {
    return identifier;
  }
}
