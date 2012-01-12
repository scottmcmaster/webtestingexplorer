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

import com.google.common.collect.Lists;
import com.google.testing.webtestingexplorer.identifiers.WebElementIdentifier;
import com.google.testing.webtestingexplorer.identifiers.WebElementWithIdentifier;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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

  abstract boolean areElementsValid(Collection<WebElementWithIdentifier> elements);

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

    for (Map.Entry<WebElementIdentifier, Map<String, String>> entry : elementProperties.entrySet()) {
      Map<String, String> otherProperties = otherState.elementProperties.get(entry.getKey());
      if (otherProperties == null) {
        return false;
      }

      Map<String, String> theseProperties = entry.getValue();
      
      if (theseProperties.size() != otherProperties.size()) {
        return false;
      }

      for (Map.Entry<String, String> thesePropertyEntry : theseProperties.entrySet()) {
        String key = thesePropertyEntry.getKey();
        
        if (!thesePropertyEntry.getValue().equalsIgnoreCase(otherProperties.get(key))) {
          return false;
        }
      }
    }

    return true;
  }
  
  @Override
  public List<StateDifference> diff(State other) {
    // TODO(xyuan): Create a unit test.
    if (!(other.getClass().getName().equalsIgnoreCase(this.getClass().getName()))) {
      throw new IllegalArgumentException("Invalid state class: " + other.getClass().getName());
    }
    List<StateDifference> result = Lists.newArrayList();
    
    ElementsState otherState = (ElementsState)other;
    diffOneWay(this, otherState, true, result);
    diffOneWay(otherState, this, false, result);
   
    return result;
  }
  
  private void diffOneWay(ElementsState thisState, ElementsState otherState, 
      boolean leftToRight, List<StateDifference> stateDiff) {
    for (Map.Entry<WebElementIdentifier, Map<String, String>> entry : thisState.elementProperties.entrySet()) {
      WebElementIdentifier identifier = entry.getKey();
      Map<String, String> theseProperties = entry.getValue();     
      Map<String, String> otherProperties = otherState.elementProperties.get(identifier);
      
      // Check if this difference has already been recorded
      String diffKey = "ElementDiff:" + identifier.toString();
      if (stateDiff.contains(diffKey)) {
        continue;
      }
        
      // Check existence of an element
      if (otherProperties == null) {
        if (leftToRight) {
          stateDiff.add(new PropertyValueStateDifference(diffKey, theseProperties, ""));
        } else {
          stateDiff.add(new PropertyValueStateDifference(diffKey, "", theseProperties));
        }
      } else {  
        // Check whether the element has same set of properties
        for (Map.Entry<String, String> thesePropertyEntry : theseProperties.entrySet()) {
          String key = thesePropertyEntry.getKey();
          String value = thesePropertyEntry.getValue();
          String otherValue = otherProperties.get(key);
          
          // Check if this difference has already been recorded
          diffKey = "PropertyDiff: identity-" + identifier.toString() + " property-" + key;
          if (stateDiff.contains(diffKey)) {
            continue;
          }
          if (otherValue == null) {
            if (leftToRight) {
              stateDiff.add(new PropertyValueStateDifference(diffKey, value, ""));
            } else {
              stateDiff.add(new PropertyValueStateDifference(diffKey, "", value));
            }
          } else {
            // Check if this difference has already been recorded
            diffKey = "PropertyValueDiff: identity-" + identifier.toString() + " property-" + key;
            if (stateDiff.contains(diffKey)) {
              continue;
            }
            if (!value.equalsIgnoreCase(otherProperties.get(key))) {
              stateDiff.add(new PropertyValueStateDifference(diffKey, value, otherProperties.get(key)));
            }
          }
        }// for
      }    
    }// for
  }
}
