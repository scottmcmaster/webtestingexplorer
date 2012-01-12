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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests for the {@link PropertyValueStateDifference} class.
 *  
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
public class PropertyValueStateDifferenceTest {

  @Test
  public void formatFirstValue() {
    PropertyValueStateDifference diff = new PropertyValueStateDifference("prop", "first", null);
    assertEquals("first", diff.formatFirstValue());
    
    diff = new PropertyValueStateDifference("prop", null, null);
    assertEquals("null", diff.formatFirstValue());
  }

  @Test
  public void formatSecondValue() {
    PropertyValueStateDifference diff = new PropertyValueStateDifference("prop", null, "second");
    assertEquals("second", diff.formatSecondValue());
    
    diff = new PropertyValueStateDifference("prop", null, null);
    assertEquals("null", diff.formatSecondValue());
  }

  @Test
  public void formatDifference() {
    PropertyValueStateDifference diff = new PropertyValueStateDifference("prop", "first", "second");
    assertEquals("DiffKey:prop V1:first V2:second", diff.formatDifference());
  }
}
