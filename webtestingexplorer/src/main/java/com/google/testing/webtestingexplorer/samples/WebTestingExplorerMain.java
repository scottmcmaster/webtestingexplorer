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
package com.google.testing.webtestingexplorer.samples;

import com.google.common.collect.Sets;
import com.google.testing.webtestingexplorer.actions.Action;
import com.google.testing.webtestingexplorer.actions.SetTextAction;
import com.google.testing.webtestingexplorer.config.IdentifierActionGeneratorConfig;
import com.google.testing.webtestingexplorer.config.JavascriptAnchorActionGeneratorConfig;
import com.google.testing.webtestingexplorer.config.MaxRepeatedActionSequenceFilter;
import com.google.testing.webtestingexplorer.config.WebTestingConfig;
import com.google.testing.webtestingexplorer.explorer.WebTestingExplorer;
import com.google.testing.webtestingexplorer.identifiers.NameWebElementIdentifier;
import com.google.testing.webtestingexplorer.identifiers.WebElementWithIdentifier;
import com.google.testing.webtestingexplorer.state.CountOfElementsStateChecker;
import com.google.testing.webtestingexplorer.testcase.TestCaseWriter;

import java.util.Set;

/**
 * Entry point for the application that tries things out.
 * 
 * @author smcmaster@google.com (Scott McMaster)
 */
public class WebTestingExplorerMain {

  public static void main(String[] args) throws Exception {
    String url = args[0];
    WebTestingConfig config = new WebTestingConfig()
        .setTestCaseWriter(new TestCaseWriter("/tmp/webtestexplorer"))
        .setUrl(url)
        .setMaxLength(5)
        .addStateChecker(new CountOfElementsStateChecker())
        .setOracleConfigFactory(new SampleOracleConfigFactory())
        .withRefreshButtonAction()
        .addActionSequenceFilter(new MaxRepeatedActionSequenceFilter(2))
        .addActionGeneratorConfig(new IdentifierActionGeneratorConfig(
            new NameWebElementIdentifier("feedback_email")) {          
          @Override
          public Set<Action> generateActions(WebElementWithIdentifier elementWithId) {
            // Try valid and invalid email addresses.
            Set<Action> actions = Sets.newHashSet();
            actions.add(new SetTextAction(elementWithId.getIdentifier(), "bob@example.com"));
            actions.add(new SetTextAction(elementWithId.getIdentifier(), "invalid_email"));
            return actions;
          }
        })
        .addActionGeneratorConfig(new JavascriptAnchorActionGeneratorConfig());
    new WebTestingExplorer(config).run();    
  }
}
