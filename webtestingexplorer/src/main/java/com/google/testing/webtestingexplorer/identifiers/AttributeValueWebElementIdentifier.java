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

/**
 * Finds a webelement with the specified attribute having the specified value.
 * Uses Xpath.
 * 
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
public class AttributeValueWebElementIdentifier extends XpathWebElementIdentifier {

  public AttributeValueWebElementIdentifier(String name, String value) {
    this(name, value, null, null);
  }
  
  public AttributeValueWebElementIdentifier(String name, String value, String frameIdentifier,
      String tagName) {
    super(String.format("//*[@%s='%s']", name, value), frameIdentifier, tagName);
  }
}
