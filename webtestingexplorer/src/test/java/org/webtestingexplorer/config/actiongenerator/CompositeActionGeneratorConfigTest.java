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
package org.webtestingexplorer.config.actiongenerator;

import static org.junit.Assert.*;

import com.google.common.collect.Sets;

import org.junit.Test;
import org.webtestingexplorer.actions.Action;
import org.webtestingexplorer.actions.BackAction;
import org.webtestingexplorer.actions.ForwardAction;
import org.webtestingexplorer.config.ActionGeneratorConfig;
import org.webtestingexplorer.config.actiongenerator.CompositeActionGeneratorConfig;
import org.webtestingexplorer.driver.WebDriverWrapper;
import org.webtestingexplorer.identifiers.WebElementWithIdentifier;

import java.util.Set;

/**
 * Tests for the {@link CompositeActionGeneratorConfig} class.
 * 
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
public class CompositeActionGeneratorConfigTest {

  @Test
  public void matches() {
    CompositeActionGeneratorConfig config = createCompositeConfig(true);
    assertTrue(config.matches(null));
    Set<Action> result = config.generateActions(null);
    assertTrue(result.contains(new BackAction()));
    assertTrue(result.contains(new ForwardAction()));
  }

  @Test
  public void noMatches() {
    CompositeActionGeneratorConfig config = createCompositeConfig(false);
    assertFalse(config.matches(null));
  }
  
  private CompositeActionGeneratorConfig createCompositeConfig(final boolean shouldMatch) {
    CompositeActionGeneratorConfig config = new CompositeActionGeneratorConfig(
        new ActionGeneratorConfig() {
          
          @Override
          public boolean matches(WebElementWithIdentifier elementWithId) {
            return shouldMatch;
          }
          
          @Override
          public Set<Action> generateActions(WebElementWithIdentifier elementWithId) {
            return Sets.<Action>newHashSet(new BackAction());
          }
          
          @Override
          public boolean isActive(WebDriverWrapper driver) {
            return true;
          }
        },
        new ActionGeneratorConfig() {
          
          @Override
          public boolean matches(WebElementWithIdentifier elementWithId) {
            return true;
          }
          
          @Override
          public Set<Action> generateActions(WebElementWithIdentifier elementWithId) {
            return Sets.<Action>newHashSet(new ForwardAction());
          }
          
          @Override
          public boolean isActive(WebDriverWrapper driver) {
            return true;
          }
        });
    return config;
  }  
}
