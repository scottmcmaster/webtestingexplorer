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
package org.webtestingexplorer.actions;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.webtestingexplorer.config.ActionGeneratorConfig;
import org.webtestingexplorer.driver.WebDriverWrapper;
import org.webtestingexplorer.identifiers.WebElementIdentifier;
import org.webtestingexplorer.identifiers.WebElementWithIdentifier;

import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Generates possible {@link Action}s given an element and a configuration.
 * 
 * @author smcmaster@google.com (Scott McMaster)
 */
public class ActionGenerator {

  private final static Logger LOGGER = Logger.getLogger(ActionGenerator.class.getName());

  /**
   * Builds the list of actions to take on a given element.
   */
  public Set<Action> generateActionsForElement(WebDriverWrapper driver,
      List<ActionGeneratorConfig> actionGeneratorConfigs,
      WebElementWithIdentifier elementWithId) {
    
    WebElement element = elementWithId.safeGetElement(driver);
    WebElementIdentifier identifier = elementWithId.getIdentifier();
    String type = element.getAttribute("type");
    String role = element.getAttribute("role");
    String ariaDisabled = element.getAttribute("aria-disabled");
    
    // WebDriver can only interact with visible elements.
    if (!element.isDisplayed() || !element.isEnabled()) {
      return Sets.newHashSet();
    }
    
    Set<Action> actions = Sets.newHashSet();
    
    // Look for a specific action configuration.
    for (ActionGeneratorConfig actionConfig : actionGeneratorConfigs) {
      if (actionConfig.matches(elementWithId)) {
        return actionConfig.generateActions(elementWithId);
      }
    }
    
    // Otherwise generate the default actions.
    if ("input".equals(element.getTagName())) {
      if ("submit".equals(type)) {
        actions.add(new ClickAction(identifier));
      } else if ("text".equals(type) || "password".equals(type)) {
        actions.addAll(createDefaultTextWidgetActions(identifier));
      } else if ("checkbox".equals(type) || "radio".equals(type)) {
        // TODO(smcmaster): I think ClickAction should be fine for these and they don't
        // need their own specific action types. But I need to confirm on an example...
        actions.add(new ClickAction(identifier));
      }
    } else if ("textarea".equals(element.getTagName())) {
      actions.addAll(createDefaultTextWidgetActions(identifier));
    } else if ("a".equals(element.getTagName())) {
      actions.add(new ClickAction(identifier));
    } else if ("button".equals(element.getTagName()) ||
        ("button".equals(role) && (ariaDisabled == null || "false".equals(ariaDisabled)))) {
      actions.add(new ClickAction(identifier));      
    } else if ("select".equals(element.getTagName())) {
      // Default to selecting each of the first two options.
      // TODO(smcmaster): Enhance the API to allow customizing this behavior.
      Select select = new Select(element);
      int numActions = 0;
      if (select.getOptions().size() >= 0) {
        ++numActions;
      }
      if (select.getOptions().size() >= 1) {
        ++numActions;
      }
      actions.addAll(createDefaultSelectWidgetActions(identifier, numActions));
    }
    LOGGER.log(Level.FINE, "Generated actions for element " + elementWithId.getIdentifier());
    return actions;
  }

  /**
   * Creates the requested number of SelectActions for the given element.
   */
  public static List<Action> createDefaultSelectWidgetActions(
      WebElementIdentifier identifier, int numActions) {
    List<Action> actions = Lists.newArrayList();
    for (int i = 0; i < numActions; ++i) {
      actions.add(new SelectAction(identifier, i));
    }
    return actions;
  }

  /**
   * Creates the standard SetTextAction for the given element.
   */
  public static List<Action> createDefaultTextWidgetActions(WebElementIdentifier identifier) {
    return Lists.<Action>newArrayList(new SetTextAction(identifier, "TESTING"));
  }
}
