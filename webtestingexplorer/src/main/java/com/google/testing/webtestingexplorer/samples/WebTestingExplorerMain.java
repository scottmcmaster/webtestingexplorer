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

import com.google.testing.webtestingexplorer.actions.Action;
import com.google.testing.webtestingexplorer.actions.ClickAction;
import com.google.testing.webtestingexplorer.actions.SetTextAction;
import com.google.testing.webtestingexplorer.config.NameActionGeneratorConfig;
import com.google.testing.webtestingexplorer.config.OracleConfig;
import com.google.testing.webtestingexplorer.config.TagActionGeneratorConfig;
import com.google.testing.webtestingexplorer.config.WebTestingConfig;
import com.google.testing.webtestingexplorer.explorer.WebTestingExplorer;
import com.google.testing.webtestingexplorer.identifiers.WebElementIdentifier;
import com.google.testing.webtestingexplorer.oracles.HttpStatusCodeOracle;
import com.google.testing.webtestingexplorer.oracles.JSErrorCollectorOracle;
import com.google.testing.webtestingexplorer.state.CountOfElementsStateChecker;
import com.google.testing.webtestingexplorer.testcase.TestCaseWriter;

import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Entry point for the application that tries things out.
 * 
 * @author smcmaster@google.com (Scott McMaster)
 */
public class WebTestingExplorerMain {

  public static void main(String[] args) throws Exception {
    String url = args[0];
    OracleConfig oracleConfig = new OracleConfig()
        .addAfterActionOracle(new HttpStatusCodeOracle().setDisallowedStatusCodes(500, 503))
        .addAfterActionOracle(new JSErrorCollectorOracle());
    WebTestingConfig config = new WebTestingConfig()
        .setTestCaseWriter(new TestCaseWriter("/tmp/webtestexplorer"))
        .setUrl(url)
        .setMaxLength(5)
        .addStateChecker(new CountOfElementsStateChecker())
        .setOracleConfig(oracleConfig)
        .addActionGeneratorConfig(new NameActionGeneratorConfig("feedback_email") {
          @Override
          public List<Action> generateActions(WebElement element, WebElementIdentifier identifier) {
            // Try valid and invalid email addresses.
            List<Action> actions = new ArrayList<Action>();
            actions.add(new SetTextAction(identifier, "bob@example.com"));
            actions.add(new SetTextAction(identifier, "invalid_email"));
            return actions;
          }
        })
        .addActionGeneratorConfig(new TagActionGeneratorConfig("a") {
          @Override
          public List<Action> generateActions(WebElement element, WebElementIdentifier identifier) {
            // Only click on "javascript:" anchors.
            List<Action> actions = new ArrayList<Action>();
            String href = element.getAttribute("href");
            if (href != null && href.startsWith("javascript:")) {
              actions.add(new ClickAction(identifier));
            }
            return actions;
          }
        });
    new WebTestingExplorer(config).run();    
  }
}
