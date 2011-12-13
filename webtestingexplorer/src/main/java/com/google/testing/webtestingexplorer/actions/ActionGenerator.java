/*
Copyright 2011 Google Inc. All Rights Reserved.

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
package com.google.testing.webtestingexplorer.actions;

import com.google.testing.webtestingexplorer.config.ActionGeneratorConfig;
import com.google.testing.webtestingexplorer.config.WebTestingConfig;
import com.google.testing.webtestingexplorer.driver.WebDriverWrapper;
import com.google.testing.webtestingexplorer.identifiers.WebElementIdentifier;
import com.google.testing.webtestingexplorer.identifiers.WebElementWithIdentifier;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.List;

/**
 * Generates possible {@link Action}s given an element and a configuration.
 * 
 * @author smcmaster@google.com (Scott McMaster)
 */
public class ActionGenerator {

  private WebTestingConfig config;

  public ActionGenerator(WebTestingConfig config) {
    this.config = config;
  }
  
  /**
   * Builds the list of actions to take on a given element.
   */
  public List<Action> generateActionsForElement(WebDriverWrapper driver,
      WebElementWithIdentifier elementWithId) {
    WebElement element = elementWithId.getElement();
    WebElementIdentifier identifier = elementWithId.getIdentifier();
    String type = element.getAttribute("type");
    String role = element.getAttribute("role");
    String ariaDisabled = element.getAttribute("aria-disabled");
    
    // WebDriver can only interact with visible elements.
    if (!element.isDisplayed() || !element.isEnabled()) {
      return new ArrayList<Action>();
    }
    
    List<Action> actions = new ArrayList<Action>();
    
    // Look for a specific action configuration.
    for (ActionGeneratorConfig actionConfig : config.getActionGeneratorConfigs()) {
      if (actionConfig.matches(element, identifier)) {
        return actionConfig.generateActions(element, identifier);
      }
    }
    
    // Otherwise generate the default actions.
    if ("input".equals(element.getTagName())) {
      if ("submit".equals(type)) {
        actions.add(new ClickAction(identifier));
      } else if ("text".equals(type) || "password".equals(type)) {
        actions.add(new SetTextAction(identifier, "TESTING"));
      } else if ("checkbox".equals(type) || "radio".equals(type)) {
        // TODO(smcmaster): I think ClickAction should be fine for these and they don't
        // need their own specific action types. But I need to confirm on an example...
        actions.add(new ClickAction(identifier));
      }
    } else if ("textarea".equals(element.getTagName())) {
      actions.add(new SetTextAction(identifier, "TESTING"));
    } else if ("a".equals(element.getTagName())) {
      actions.add(new ClickAction(identifier));
    } else if ("button".equals(element.getTagName()) ||
        ("button".equals(role) && (ariaDisabled == null || "false".equals(ariaDisabled)))) {
      actions.add(new ClickAction(identifier));      
    } else if ("select".equals(element.getTagName())) {
      // Default to selecting each of the first two options.
      // TODO(smcmaster): Enhance the API to allow customizing this behavior.
      Select select = new Select(element);
      if (select.getOptions().size() >= 0) {
        actions.add(new SelectAction(identifier, 0));
      }
      if (select.getOptions().size() >= 1) {
        actions.add(new SelectAction(identifier, 1));
      }
    }
    return actions;
  }
}
