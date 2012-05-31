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

package org.webtestingexplorer.config;

import com.google.common.collect.Sets;

import org.openqa.selenium.WebElement;
import org.webtestingexplorer.driver.WebDriverWrapper;
import org.webtestingexplorer.driver.WebElementWrapper;
import org.webtestingexplorer.identifiers.WebElementIdentifier;

import java.util.List;
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
  
  /**
   * Gets all the elements picked out as equivalent in the current state
   * of the given driver.
   */
  public Set<WebElement> getEquivalentElements(WebDriverWrapper driver) {
    Set<WebElement> allElementsWithIds = Sets.newHashSet();
    for (WebElementIdentifier identifier : equivalentElementIdentifiers) {
      List<WebElementWrapper> elementsWithIds = identifier.findElements(driver);
      allElementsWithIds.addAll(elementsWithIds);
    }
    return allElementsWithIds;
  }
}