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
import org.webtestingexplorer.config.actiongenerator.TagActionGeneratorConfig;
import org.webtestingexplorer.driver.WebDriverWrapper;
import org.webtestingexplorer.identifiers.WebElementIdentifier;
import org.webtestingexplorer.identifiers.WebElementWithIdentifier;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Generates possible {@link Action}s given an element and a configuration.
 * 
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
public class ActionGenerator {

  private final static Logger LOGGER = Logger.getLogger(ActionGenerator.class.getName());

  private List<ActionGeneratorConfig> defaultActionGeneratorConfigs;
  
  public ActionGenerator() {
    defaultActionGeneratorConfigs = Lists.newArrayList();
    defaultActionGeneratorConfigs.add(new DefaultButtonActionGeneratorConfig());
    defaultActionGeneratorConfigs.add(new DefaultInputActionGeneratorConfig());
    defaultActionGeneratorConfigs.add(new DefaultSelectActionGeneratorConfig());
    defaultActionGeneratorConfigs.add(new DefaultTextAreaActionGeneratorConfig());
  }
  
  /**
   * Builds the list of actions to take on a given element.
   */
  public Set<Action> generateActionsForElement(WebDriverWrapper driver,
      List<ActionGeneratorConfig> customActionGeneratorConfigs,
      WebElementWithIdentifier elementWithId) {
    
    // WebDriver can only interact with visible elements.
    WebElement element = elementWithId.safeGetElement(driver);
    if (!element.isDisplayed() || !element.isEnabled()) {
      return Sets.newHashSet();
    }
        
    // We set this up to first look for custom action generator configs
    // in the reverse order that they were added. Then if we haven't
    // matched any custom ones, we check the default ones.
    List<ActionGeneratorConfig> allActionGeneratorConfigs = Lists.newArrayList();
    allActionGeneratorConfigs.addAll(customActionGeneratorConfigs);
    Collections.reverse(allActionGeneratorConfigs);
    allActionGeneratorConfigs.addAll(defaultActionGeneratorConfigs);
    
    for (ActionGeneratorConfig actionConfig : allActionGeneratorConfigs) {
      if (actionConfig.matches(elementWithId)) {
        LOGGER.log(Level.FINE, "Generated actions for element " + elementWithId.getIdentifier());
        return actionConfig.generateActions(elementWithId);
      }
    }
    LOGGER.log(Level.FINE, "No actions generated for element " + elementWithId.getIdentifier());
    return Sets.newHashSet();
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
  
  private static class DefaultInputActionGeneratorConfig extends TagActionGeneratorConfig {
    public DefaultInputActionGeneratorConfig() {
      super("input");
    }
    
    @Override
    public Set<Action> generateActions(WebElementWithIdentifier elementWithId) {
      Set<Action> actions = Sets.newHashSet();
      String type = elementWithId.getElement().getAttribute("type");
      if ("submit".equals(type)) {
        actions.add(new ClickAction(elementWithId.getIdentifier()));
      } else if ("text".equals(type) || "password".equals(type)) {
        actions.addAll(createDefaultTextWidgetActions(elementWithId.getIdentifier()));
      } else if ("checkbox".equals(type) || "radio".equals(type)) {
        actions.add(new ClickAction(elementWithId.getIdentifier()));
      }
      return actions;
    }
  }
  
  private static class DefaultTextAreaActionGeneratorConfig extends TagActionGeneratorConfig {
    public DefaultTextAreaActionGeneratorConfig() {
      super("textarea");
    }
    
    @Override
    public Set<Action> generateActions(WebElementWithIdentifier elementWithId) {
      Set<Action> actions = Sets.newHashSet();
      actions.addAll(createDefaultTextWidgetActions(elementWithId.getIdentifier()));
      return actions;
    }
  }
  
  private static class DefaultSelectActionGeneratorConfig extends TagActionGeneratorConfig {
    public DefaultSelectActionGeneratorConfig() {
      super("select");
    }
    
    @Override
    public Set<Action> generateActions(WebElementWithIdentifier elementWithId) {
      Set<Action> actions = Sets.newHashSet();
      // Default to selecting each of the first two options.
      // TODO(smcmaster): Enhance the API to allow customizing this behavior.
      Select select = new Select(elementWithId.getElement());
      int numActions = 0;
      if (select.getOptions().size() >= 0) {
        ++numActions;
      }
      if (select.getOptions().size() >= 1) {
        ++numActions;
      }
      actions.addAll(createDefaultSelectWidgetActions(elementWithId.getIdentifier(), numActions));
      return actions;
    }
  }
  
  private static class DefaultButtonActionGeneratorConfig extends TagActionGeneratorConfig {
    public DefaultButtonActionGeneratorConfig() {
      super("button");
    }
    
    @Override
    public Set<Action> generateActions(WebElementWithIdentifier elementWithId) {
      String role = elementWithId.getElement().getAttribute("role");
      String ariaDisabled = elementWithId.getElement().getAttribute("aria-disabled");
      Set<Action> actions = Sets.newHashSet();
      if ("button".equals(role) && (ariaDisabled == null || "false".equals(ariaDisabled))) {
        actions.add(new ClickAction(elementWithId.getIdentifier()));      
      }
      return actions;
    }
  }
}
