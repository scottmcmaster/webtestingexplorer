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

import org.junit.Test;
import org.webtestingexplorer.identifiers.IdWebElementIdentifier;
import org.webtestingexplorer.identifiers.IndexWebElementIdentifier;
import org.webtestingexplorer.identifiers.WebElementIdentifier;
import org.webtestingexplorer.identifiers.XpathWebElementIdentifier;
import org.webtestingexplorer.state.MissingPropertyStateDifference;


/**
 * Tests for the {@link MissingPropertyStateDifference} class.
 *  
 * @author xyuan@google.com (Xun Yuan)
 */
public class MissingPropertyStateDifferenceTest {

  @Test
  public void formatFirstValue() {
    WebElementIdentifier identifier = new IdWebElementIdentifier("testID");
    MissingPropertyStateDifference diff = 
        new MissingPropertyStateDifference(identifier.toString(), "first", null);
    assertEquals("first", diff.formatFirstValue());
    assertEquals(identifier.toString(), diff.getProperty());
    
    diff = new MissingPropertyStateDifference(identifier.toString(), null, null);
    assertEquals("null", diff.formatFirstValue());
  }

  @Test
  public void formatSecondValue() {
    WebElementIdentifier identifier = new IndexWebElementIdentifier(12);
    MissingPropertyStateDifference diff = new MissingPropertyStateDifference(identifier.toString(), null, "second");
    assertEquals("second", diff.formatSecondValue());
    assertEquals(identifier.toString(), diff.getProperty());
    
    diff = new MissingPropertyStateDifference(identifier.toString(), null, null);
    assertEquals("null", diff.formatSecondValue());
  }

  @Test
  public void formatDifference() {
    WebElementIdentifier identifier = new XpathWebElementIdentifier("//div/a[1]");
    MissingPropertyStateDifference diff = new MissingPropertyStateDifference(identifier.toString(), "first", "second");
    assertEquals("DiffKey:prop-xpath=//div/a[1] V1:first V2:second", diff.formatDifference());
  }
}
