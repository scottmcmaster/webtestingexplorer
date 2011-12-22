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
