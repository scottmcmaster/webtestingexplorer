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
package com.google.testing.webtestingexplorer.state;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.testing.webtestingexplorer.state.CountOfElementsState;
import com.google.testing.webtestingexplorer.state.CustomizedPropertiesElementsState;

/**
 * Tests for the {@link ElementsState} class.
 *  
 * @author xyuan@gmail.com (Xun Yuan)
 */
public class ElementsStateTest {

  @Test
  public void equals() {
    String xmlString1 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><elements>" + 
        "<element><xpath>xpath1</xpath><attribute class=\"A\" value=\"B\" /></element>" +
        "</elements>";         
    CustomizedPropertiesElementsState state1 = 
        new CustomizedPropertiesElementsState(null, xmlString1); 
    // Same states
    assertTrue(state1.diff(state1).isEmpty());
    // Compare with non-ElementsState instance
    assertFalse(state1.equals(xmlString1));
    
    String xmlString2 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><elements>" + 
        "<element><xpath>xpath1</xpath><attribute class=\"A\" /><attribute value=\"B\" /></element>" +
        "<element><xpath>xpath2</xpath><attribute class=\"C\" /><attribute value=\"D\" /></element>" +
        "</elements>";
    CustomizedPropertiesElementsState state2 = 
        new CustomizedPropertiesElementsState(null, xmlString2);
    // States contain different number of WebElements
    assertFalse(state1.equals(state2));
  }
  
  @Test
  public void diff() {
    CountOfElementsState state0 = new CountOfElementsState(1);
    
    String xmlString1 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><elements>" + 
        "<element><xpath>xpath1</xpath><attribute class=\"A\" value=\"B\" /></element>" +
        "</elements>";         
    CustomizedPropertiesElementsState state1 = 
        new CustomizedPropertiesElementsState(null, xmlString1); 
    
    try {
      state0.diff(state1);
      assertTrue(false);
    } catch (IllegalArgumentException e) {
      assertEquals(e.getMessage(), 
          "Invalid state class: com.google.testing.webtestingexplorer.state.CustomizedPropertiesElementsState");
    }

    // There is at least one WebElement in one state but not the other
    String xmlString2 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><elements>" + 
        "<element><xpath>xpath2</xpath><attribute class=\"A\" value=\"B\" /></element>" +
        "</elements>";
    CustomizedPropertiesElementsState state2 = 
        new CustomizedPropertiesElementsState(null, xmlString2);
    assertFalse(state1.diff(state2).isEmpty());
      
    xmlString2 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><elements>" + 
        "<element><xpath>xpath1</xpath><attribute class=\"A\" value=\"B\" /></element>" +
        "<element><xpath>xpath2</xpath><attribute class=\"C\" value=\"D\" /></element>" +
        "</elements>";
    state2 = new CustomizedPropertiesElementsState(null, xmlString2);
    assertFalse(state1.equals(state2));
    
    // There is at least one common WebElement that contains a property in one state but
    // not the other
    xmlString2 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><elements>" + 
        "<element><xpath>xpath1</xpath><attribute class=\"A\" /></element>" +
        "</elements>";
    state2 = new CustomizedPropertiesElementsState(null, xmlString2);
    assertFalse(state1.diff(state2).isEmpty());
    
    xmlString2 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><elements>" + 
        "<element><xpath>xpath1</xpath><attribute class=\"A\" value=\"B\" tagName=\"C\" /></element>" +
        "</elements>";
    state2 = new CustomizedPropertiesElementsState(null, xmlString2);
    assertFalse(state1.diff(state2).isEmpty());
    
    // There is at least one common WebElement that contains a propety that has different
    // values
    xmlString2 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><elements>" + 
        "<element><xpath>xpath1</xpath><attribute class=\"E\" value=\"B\" /></element>" +
        "</elements>";
    state2 = new CustomizedPropertiesElementsState(null, xmlString2);
    assertFalse(state1.diff(state2).isEmpty());
  }

}