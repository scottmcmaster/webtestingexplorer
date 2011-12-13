/*
 * Copyright 2011 Google Inc. All Rights Reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.testing.webtestingexplorer.state;

import java.util.List;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import org.openqa.selenium.WebElement;

import com.google.testing.webtestingexplorer.identifiers.WebElementIdentifier;

/**
 * Parent class of element related state.
 * 
 * @author xyuan@google.com (Xun Yuan)
 */
public abstract class ElementsState implements State {

  protected enum ElementType {
    ALL, VISIBLE, ACTIONABLE
  }
  
  protected final static String ID = "id";
  protected final static String TAG_NAME = "tagName";
  protected final static String CLASS = "class";
  protected final static String TYPE = "type";
  protected final static String TITLE = "title";
  protected final static String ROLE = "rold";
  protected final static String STYLE = "style";
  protected final static String VALUE = "value";
  protected final static String ENABLED = "enabled";
  protected final static String SELECTED = "selected";

  // TODO(xyuan): These should be configurable.
  protected static final List<String> ELEMENT_ATTRIBUTES = Arrays.asList(
     ID, CLASS, TYPE, TITLE, ROLE, STYLE);

  Map<WebElementIdentifier, Map<String, String>> elementProperties;

  ElementType elementType;

  abstract boolean areElementsValid(Collection<WebElement> elements);

  

  /**
   * Compare whether two states are equal.
   */
  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    if (!(other instanceof ElementsState)) {
      return false;
    }

    ElementsState otherState = (ElementsState) other;
    int propertiesSize = elementProperties.size();
    if (propertiesSize != otherState.elementProperties.size()) {
      return false;
    }

    boolean isEqual = true;
    for (Map.Entry<WebElementIdentifier, Map<String, String>> entry : elementProperties.entrySet()) {
      Map<String, String> otherProperties = otherState.elementProperties.get(entry.getKey());
      if (otherProperties == null) {
        isEqual = false;
        break;
      }

      Map<String, String> theseProperties = entry.getValue();
      
      if (theseProperties.size() != otherProperties.size()) {
        isEqual = false;
        break;
      }

      for (Map.Entry<String, String> thesePropertyEntry : theseProperties.entrySet()) {
        String key = thesePropertyEntry.getKey();
        
        if (!thesePropertyEntry.getValue().equalsIgnoreCase(otherProperties.get(key))) {
          isEqual = false;
          break;
        }
      }
      if (!isEqual) {
        break;
      }
    }

    return isEqual;
  }
}
