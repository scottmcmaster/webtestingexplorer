// Copyright 2011 Google Inc. All Rights Reserved.

package com.google.testing.webtestingexplorer.config;

import com.google.testing.webtestingexplorer.identifiers.WebElementIdentifier;
import com.google.testing.webtestingexplorer.identifiers.WebElementWithIdentifier;

/**
 * Action generator config that picks out elements using our identifier scheme.
 * 
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
public abstract class IdentifierActionGeneratorConfig implements ActionGeneratorConfig {

  private WebElementIdentifier identifier;
  
  public IdentifierActionGeneratorConfig(WebElementIdentifier identifier) {
    this.identifier = identifier;
  }
  
  @Override
  public boolean matches(WebElementWithIdentifier elementWithId) {
    return identifier.equals(elementWithId.getIdentifier());
  }
}
