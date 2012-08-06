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
import org.webtestingexplorer.actions.ClickAction;
import org.webtestingexplorer.config.ActionGeneratorConfig;
import org.webtestingexplorer.identifiers.WebElementWithIdentifier;

import java.util.Set;

/**
 * An {@link ActionGeneratorConfig} for anchors that only clicks them if they
 * are javascript: ones.
 * 
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
public final class JavascriptAnchorActionGeneratorConfig extends TagActionGeneratorConfig {

  public JavascriptAnchorActionGeneratorConfig() {
    super("a");
  }

  @Override
  public Set<Action> generateActions(WebElementWithIdentifier elementWithId) {
    // Only click on "javascript:" anchors.
    Set<Action> actions = Sets.newHashSet();
    String href = elementWithId.getElement().getAttribute("href");
    if (href != null && href.startsWith("javascript:")) {
      actions.add(new ClickAction(elementWithId.getIdentifier()));
    }
    return actions;
  }
}
