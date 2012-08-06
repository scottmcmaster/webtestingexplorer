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

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import org.webtestingexplorer.config.WebElementSelectorRegistry;
import org.webtestingexplorer.config.selector.WebElementSelector;
import org.webtestingexplorer.identifiers.WebElementIdentifier;
import org.webtestingexplorer.identifiers.WebElementWithIdentifier;

import java.util.List;
import java.util.Set;

/**
 * Abstracts state as a collection of web element identifiers.
 * 
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
public class SelectedElementsState implements State {

  private final Set<WebElementIdentifier> identifiers;
  private final String selectorKey;
  
  public SelectedElementsState(List<WebElementWithIdentifier> elements,
      String selectorKey) {
    this.selectorKey = selectorKey;
    identifiers = Sets.newHashSet();
    for (WebElementWithIdentifier element : elements) {
      identifiers.add(element.getIdentifier());
    }
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    if (!(other instanceof SelectedElementsState)) {
      return false;
    }
    return diff((SelectedElementsState) other).isEmpty();
  }

  @Override
  public StateChecker createStateChecker() {
    WebElementSelector selector = WebElementSelectorRegistry.getInstance().get(selectorKey);
    if (selector == null) {
      throw new IllegalStateException("Selector is not registered: " + selectorKey);
    }
    return new SelectedElementsStateChecker(selector);
  }

  @Override
  public List<StateDifference> diff(State otherState) {
    if (!(otherState instanceof SelectedElementsState)) {
      throw new IllegalArgumentException("Invalid state class: " + otherState.getClass().getName());
    }
    List<StateDifference> stateDiff = Lists.newArrayList();
    SelectedElementsState other = (SelectedElementsState) otherState;
    for (WebElementIdentifier identifier : identifiers) {
      if (!other.identifiers.contains(identifier)) {
        stateDiff.add(new MissingWebElementStateDifference(identifier, identifier, null));        
      }
    }
    for (WebElementIdentifier identifier : other.identifiers) {
      if (!identifiers.contains(identifier)) {
        stateDiff.add(new MissingWebElementStateDifference(identifier, null, identifier));        
      }
    }
    return stateDiff;
  }
}
