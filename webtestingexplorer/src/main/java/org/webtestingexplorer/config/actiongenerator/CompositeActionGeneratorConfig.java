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

import com.google.common.collect.Sets;

import org.webtestingexplorer.actions.Action;
import org.webtestingexplorer.config.ActionGeneratorConfig;
import org.webtestingexplorer.identifiers.WebElementWithIdentifier;

import java.util.Set;

/**
 * Composes two {@link ActionGeneratorConfig}s such that if they both match,
 * their combined actions are generated.
 * 
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
public class CompositeActionGeneratorConfig extends AbstractActionGeneratorConfig {

  private final ActionGeneratorConfig first;
  private final ActionGeneratorConfig second;

  public CompositeActionGeneratorConfig(ActionGeneratorConfig first, ActionGeneratorConfig second) {
    this.first = first;
    this.second = second;
  }
  
  @Override
  public boolean matches(WebElementWithIdentifier elementWithId) {
    return first.matches(elementWithId) && second.matches(elementWithId);
  }

  @Override
  public Set<Action> generateActions(WebElementWithIdentifier elementWithId) {
    Set<Action> firstActions = first.generateActions(elementWithId);
    Set<Action> secondActions = second.generateActions(elementWithId);
    return Sets.union(firstActions, secondActions);
  }
}
