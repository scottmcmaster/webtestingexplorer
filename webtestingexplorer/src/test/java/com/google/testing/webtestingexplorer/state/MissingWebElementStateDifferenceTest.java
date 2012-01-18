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

import com.google.testing.webtestingexplorer.identifiers.WebElementIdentifier;
import com.google.testing.webtestingexplorer.identifiers.IdWebElementIdentifier;
import com.google.testing.webtestingexplorer.identifiers.IndexWebElementIdentifier;
import com.google.testing.webtestingexplorer.identifiers.XpathWebElementIdentifier;
import com.google.testing.webtestingexplorer.state.MissingWebElementStateDifference;

/**
 * Tests for the {@link MissingWebElementStateDifference} class.
 *  
 * @author xyuan@google.com (Xun Yuan)
 */
public class MissingWebElementStateDifferenceTest {

  @Test
  public void formatFirstValue() {
    WebElementIdentifier identifier = new IdWebElementIdentifier("testID");
    MissingWebElementStateDifference diff = 
        new MissingWebElementStateDifference(identifier, "first", null);
    assertEquals("first", diff.formatFirstValue());
    assertEquals(identifier.toString(), diff.getElementIdentifier().toString());
    
    diff = new MissingWebElementStateDifference(identifier, null, null);
    assertEquals("null", diff.formatFirstValue());
  }

  @Test
  public void formatSecondValue() {
    WebElementIdentifier identifier = new IndexWebElementIdentifier(12);
    MissingWebElementStateDifference diff = new MissingWebElementStateDifference(identifier, null, "second");
    assertEquals("second", diff.formatSecondValue());
    assertEquals(identifier.toString(), diff.getElementIdentifier().toString());
    
    diff = new MissingWebElementStateDifference(identifier, null, null);
    assertEquals("null", diff.formatSecondValue());
  }

  @Test
  public void formatDifference() {
    WebElementIdentifier identifier = new XpathWebElementIdentifier("//div/a[1]");
    MissingWebElementStateDifference diff = new MissingWebElementStateDifference(identifier, "first", "second");
    assertEquals("DiffKey:element-xpath=//div/a[1] V1:first V2:second", diff.formatDifference());
  }
}
