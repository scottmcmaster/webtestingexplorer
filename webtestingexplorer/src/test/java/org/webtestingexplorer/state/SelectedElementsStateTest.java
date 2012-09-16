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
package org.webtestingexplorer.state;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Lists;

import org.junit.Before;
import org.junit.Test;
import org.webtestingexplorer.identifiers.IndexWebElementIdentifier;
import org.webtestingexplorer.identifiers.WebElementWithIdentifier;

import java.util.List;

/**
 * Tests for the {@link SelectedElementsState} class.
 * 
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
public class SelectedElementsStateTest {

  private static final String SELECTOR_CLASS_NAME = "selectorClass";

  private List<WebElementWithIdentifier> elements1 = Lists.newArrayList();
  private List<WebElementWithIdentifier> elements2 = Lists.newArrayList();
  private List<WebElementWithIdentifier> elements3 = Lists.newArrayList();

  private IndexWebElementIdentifier id3;
  private IndexWebElementIdentifier id2;
  private IndexWebElementIdentifier id1;

  @Before
  public void setUp() {
    id1 = new IndexWebElementIdentifier(0);
    WebElementWithIdentifier element1 = new WebElementWithIdentifier(null, id1);
    id2 = new IndexWebElementIdentifier(1);
    WebElementWithIdentifier element2 = new WebElementWithIdentifier(null, id2);
    id3 = new IndexWebElementIdentifier(2);
    WebElementWithIdentifier element3 = new WebElementWithIdentifier(null, id3);
    elements1.add(element1);
    elements2.add(element1);
    elements1.add(element2);
    elements2.add(element2);
    elements3.add(element3);
  }
  
  @Test
  public void equalsTest() {
    SelectedElementsState first = new SelectedElementsState(elements1, SELECTOR_CLASS_NAME);
    SelectedElementsState second = new SelectedElementsState(elements2, SELECTOR_CLASS_NAME);
    SelectedElementsState third = new SelectedElementsState(elements3, SELECTOR_CLASS_NAME);
    assertTrue(first.equals(second));
    assertTrue(second.equals(first));
    assertFalse(first.equals(third));
    assertFalse(third.equals(first));
  }

  @Test
  public void diff() {
    SelectedElementsState first = new SelectedElementsState(elements1, SELECTOR_CLASS_NAME);
    SelectedElementsState second = new SelectedElementsState(elements2, SELECTOR_CLASS_NAME);
    SelectedElementsState third = new SelectedElementsState(elements3, SELECTOR_CLASS_NAME);

    List<StateDifference> diff = first.diff(second);
    assertTrue(diff.isEmpty());
    
    diff = first.diff(third);
    assertEquals(3, diff.size());
    
    MissingPropertyStateDifference difference = (MissingPropertyStateDifference) diff.get(0);
    assertEquals(id2.toString(), difference.getProperty());
    assertEquals(id2.toString(), difference.formatFirstValue());
    assertEquals("null", difference.formatSecondValue());
    
    difference = (MissingPropertyStateDifference) diff.get(1);
    assertEquals(id1.toString(), difference.getProperty());
    assertEquals(id1.toString(), difference.formatFirstValue());
    assertEquals("null", difference.formatSecondValue());
    
    difference = (MissingPropertyStateDifference) diff.get(2);
    assertEquals(id3.toString(), difference.getProperty());
    assertEquals(id3.toString(), difference.formatSecondValue());
    assertEquals("null", difference.formatFirstValue());
  }  
}
