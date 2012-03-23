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

import java.util.Arrays;
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

  /**
   * Compare whether two states are equal.
   */
  @Override
  public boolean equals(Object other) {
    if (other == this) {
      // Same states
      return true;
    }
    if (!(other instanceof ElementsState)) {
      // Compare with non-ElementsState instance
      return false;
    }

    ElementsState otherState = (ElementsState) other;
    return this.diff(otherState).isEmpty();
  }
  
  @Override
  public List<StateDifference> diff(State other) {
    if (!(other.getClass().getName().equalsIgnoreCase(this.getClass().getName()))) {
      throw new IllegalArgumentException("Invalid state class: " + other.getClass().getName());
    }
    List<StateDifference> result = Lists.newArrayList();
    List<String> diffKeys = Lists.newArrayList();
    
    ElementsState otherState = (ElementsState)other;
    diffOneWay(this, otherState, true, result, diffKeys);
    diffOneWay(otherState, this, false, result, diffKeys);
   
    return result;
  }
  
  private void diffOneWay(ElementsState thisState, ElementsState otherState, 
      boolean leftToRight, List<StateDifference> stateDiff, List<String> diffKeys) {
    for (Map.Entry<WebElementIdentifier, Map<String, String>> entry : thisState.elementProperties.entrySet()) {
      WebElementIdentifier identifier = entry.getKey();
      Map<String, String> theseProperties = entry.getValue();     
      Map<String, String> otherProperties = otherState.elementProperties.get(identifier);
      
      // Check if this difference has already been recorded
      String diffKey = identifier.toString();
      if (diffKeys.contains(diffKey)) {
        continue;
      }
        
      // Check existence of an element
      if (otherProperties == null) {
        if (leftToRight) {
          stateDiff.add(new MissingWebElementStateDifference(identifier, theseProperties, null));        
        } else {
          stateDiff.add(new MissingWebElementStateDifference(identifier, null, theseProperties));
        }
        diffKeys.add(diffKey);
      } else {  
        // Check whether the element has same set of properties
        for (Map.Entry<String, String> thesePropertyEntry : theseProperties.entrySet()) {
          String key = thesePropertyEntry.getKey();
          String value = thesePropertyEntry.getValue();
          String otherValue = otherProperties.get(key);
          diffKey = identifier.toString() + key;
          
          // Check if this difference has already been recorded
          if (diffKeys.contains(diffKey)) {
            continue;
          }
          diffKeys.add(diffKey);
          if (otherValue == null) {
            if (leftToRight) {
              stateDiff.add(new PropertyValueStateDifference(null, key, value, null));
            } else {
              stateDiff.add(new PropertyValueStateDifference(null, key, null, value));
            }
          } else {
            if (!value.equalsIgnoreCase(otherProperties.get(key))) {
              stateDiff.add(new PropertyValueStateDifference(identifier, key, value, otherProperties.get(key)));             
            }
          }          
        }// for
      }    
    }// for
  }
}
