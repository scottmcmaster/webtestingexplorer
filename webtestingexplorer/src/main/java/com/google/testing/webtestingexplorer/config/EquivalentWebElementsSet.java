// Copyright 2011 Google Inc. All Rights Reserved.

package com.google.testing.webtestingexplorer.config;

import com.google.common.collect.Sets;
import com.google.testing.webtestingexplorer.identifiers.WebElementIdentifier;

import java.util.Set;

/**
 * Grouping of elements considered to be equivalent for purposes of testing.
 * Actions should only be generated for one element out of the entire set in
 * an equivalence class.
 * 
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
public class EquivalentWebElementsSet {

  private Set<WebElementIdentifier> equivalentElementIdentifiers;
  
  public EquivalentWebElementsSet() {
    equivalentElementIdentifiers = Sets.newHashSet();
  }

  public Set<WebElementIdentifier> getEquivalentElementIdentifiers() {
    return equivalentElementIdentifiers;
  }
  
  public void addEquivalentElementIdentifier(WebElementIdentifier identifier) {
    equivalentElementIdentifiers.add(identifier);
  }
}
