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

import java.util.List;

/**
 * Tests for the {@link CountOfElementsState} class.
 * 
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
public class CountOfElementsStateTest {

  @Test
  public void equalsTest() {
    CountOfElementsState first = new CountOfElementsState(1);
    CountOfElementsState second = new CountOfElementsState(1);
    CountOfElementsState third = new CountOfElementsState(2);
    assertTrue(first.equals(second));
    assertTrue(second.equals(first));
    assertFalse(first.equals(third));
    assertFalse(third.equals(first));
  }

  @Test
  public void diff() {
    CountOfElementsState first = new CountOfElementsState(1);
    CountOfElementsState second = new CountOfElementsState(1);
    CountOfElementsState third = new CountOfElementsState(2);
    
    List<StateDifference> diff = first.diff(second);
    assertTrue(diff.isEmpty());
    
    diff = first.diff(third);
    assertEquals(1, diff.size());
    StateDifference difference = diff.get(0);
    assertEquals("numElements", difference.getProperty());
    assertEquals(1, difference.getFirstStateValue());
    assertEquals(2, difference.getSecondStateValue());
  }  
}
