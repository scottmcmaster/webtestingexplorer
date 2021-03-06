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
package org.webtestingexplorer.config.actiongenerator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


import org.easymock.EasyMock;
import org.junit.Test;
import org.webtestingexplorer.actions.Action;
import org.webtestingexplorer.config.actiongenerator.MultiCriterionActionGeneratorConfig;
import org.webtestingexplorer.driver.WebDriverWrapper;
import org.webtestingexplorer.driver.WebElementWrapper;
import org.webtestingexplorer.identifiers.WebElementWithIdentifier;

import java.util.Set;

/**
 * Tests for the {@link MultiCriterionActionGeneratorConfig} class.
 * 
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
public class MultiCriterionActionGeneratorConfigTest {

  @Test
  public void matchesName() {
    WebElementWrapper element = createMockWebElement();
    MultiCriterionActionGeneratorConfig config = createActionGeneratorConfig(
        null, null, ".*email.*", null, null, null);
    
    assertTrue(config.matches(null, new WebElementWithIdentifier(element, null)));
    EasyMock.verify(element);
  }

  @Test
  public void noMatchesName() {
    WebElementWrapper element = createMockWebElement();
    MultiCriterionActionGeneratorConfig config = createActionGeneratorConfig(
        null, null, ".* notemail.*", null, null, null);
    
    assertFalse(config.matches(null, new WebElementWithIdentifier(element, null)));
    EasyMock.verify(element);
  }

  @Test
  public void matchesId() {
    WebElementWrapper element = createMockWebElement();
    MultiCriterionActionGeneratorConfig config = createActionGeneratorConfig(
        null, ".*email.*", null, null, null, null);
    
    assertTrue(config.matches(null, new WebElementWithIdentifier(element, null)));
    EasyMock.verify(element);
  }

  @Test
  public void noMatchesId() {
    WebElementWrapper element = createMockWebElement();
    MultiCriterionActionGeneratorConfig config = createActionGeneratorConfig(
        null, ".* notemail.*", null, null, null, null);
    
    assertFalse(config.matches(null, new WebElementWithIdentifier(element, null)));
    EasyMock.verify(element);
  }

  @Test
  public void matchesClassName() {
    WebElementWrapper element = createMockWebElement();
    MultiCriterionActionGeneratorConfig config = createActionGeneratorConfig(
        null, null, null, ".*email.*", null, null);
    
    assertTrue(config.matches(null, new WebElementWithIdentifier(element, null)));
    EasyMock.verify(element);
  }

  @Test
  public void noMatchesClassName() {
    WebElementWrapper element = createMockWebElement();
    MultiCriterionActionGeneratorConfig config = createActionGeneratorConfig(
        null, null, null, ".* notemail.*", null, null);
    
    assertFalse(config.matches(null, new WebElementWithIdentifier(element, null)));
    EasyMock.verify(element);
  }

  @Test
  public void matchesTag() {
    WebElementWrapper element = createMockWebElement();
    MultiCriterionActionGeneratorConfig config = createActionGeneratorConfig(
        "a", null, null, null, null, null);
    
    assertTrue(config.matches(null, new WebElementWithIdentifier(element, null)));
    EasyMock.verify(element);
  }

  @Test
  public void noMatchesTag() {
    WebElementWrapper element = createMockWebElement();
    MultiCriterionActionGeneratorConfig config = createActionGeneratorConfig(
        "p", null, null, null, null, null);
    
    assertFalse(config.matches(null, new WebElementWithIdentifier(element, null)));
    EasyMock.verify(element);
  }

  @Test
  public void matchesText() {
    WebElementWrapper element = createMockWebElement();
    MultiCriterionActionGeneratorConfig config = createActionGeneratorConfig(
        null, null, null, null, ".*text.*", null);
    
    assertTrue(config.matches(null, new WebElementWithIdentifier(element, null)));
    EasyMock.verify(element);
  }

  @Test
  public void noMatchesText() {
    WebElementWrapper element = createMockWebElement();
    MultiCriterionActionGeneratorConfig config = createActionGeneratorConfig(
        null, null, null, null, ".*nottext.*", null);
    
    assertFalse(config.matches(null, new WebElementWithIdentifier(element, null)));
    EasyMock.verify(element);
  }

  @Test
  public void matchesType() {
    WebElementWrapper element = createMockWebElement();
    MultiCriterionActionGeneratorConfig config = createActionGeneratorConfig(
        null, null, null, null, null, ".*pass.*");
    
    assertTrue(config.matches(null, new WebElementWithIdentifier(element, null)));
    EasyMock.verify(element);
  }

  @Test
  public void noMatchesType() {
    WebElementWrapper element = createMockWebElement();
    MultiCriterionActionGeneratorConfig config = createActionGeneratorConfig(
        null, null, null, null, null, ".*notpass.*");
    
    assertFalse(config.matches(null, new WebElementWithIdentifier(element, null)));
    EasyMock.verify(element);
  }

  @Test
  public void matchesMultiple() {
    WebElementWrapper element = createMockWebElement();
    MultiCriterionActionGeneratorConfig config = createActionGeneratorConfig(
        "a", ".*email.*", null, null, null, null);
    
    assertTrue(config.matches(null, new WebElementWithIdentifier(element, null)));
    EasyMock.verify(element);
  }

  @Test
  public void noMatchesMultiple() {
    WebElementWrapper element = createMockWebElement();
    MultiCriterionActionGeneratorConfig config = createActionGeneratorConfig(
        "p", ".*email.*", null, null, null, null);
    
    assertFalse(config.matches(null, new WebElementWithIdentifier(element, null)));
    EasyMock.verify(element);
  }

  private MultiCriterionActionGeneratorConfig createActionGeneratorConfig(String tag, String id,
      String name, String className, String text, String type) {
    MultiCriterionActionGeneratorConfig config = new MultiCriterionActionGeneratorConfig(
        tag, id, name, className, text, type) {      
      @Override
      public Set<Action> generateActions(WebDriverWrapper driver,
          WebElementWithIdentifier elementWithId) {
        return null;
      }
    };
    return config;
  }

  private WebElementWrapper createMockWebElement() {
    WebElementWrapper element = EasyMock.createNiceMock(WebElementWrapper.class);
    EasyMock.expect(element.getAttribute("name")).andReturn("feedback_email_name").anyTimes();
    EasyMock.expect(element.getAttribute("id")).andReturn("feedback_email_id").anyTimes();
    EasyMock.expect(element.getAttribute("class")).andReturn("feedback_email_class").anyTimes();
    EasyMock.expect(element.getAttribute("type")).andReturn("password").anyTimes();
    EasyMock.expect(element.getTagName()).andReturn("a").anyTimes();
    EasyMock.expect(element.getText()).andReturn("feedback_email_text").anyTimes();
    EasyMock.replay(element);
    return element;
  }
}
