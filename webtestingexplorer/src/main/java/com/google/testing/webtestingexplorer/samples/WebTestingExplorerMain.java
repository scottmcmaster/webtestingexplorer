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

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.testing.webtestingexplorer.actions.Action;
import com.google.testing.webtestingexplorer.actions.SetTextAction;
import com.google.testing.webtestingexplorer.config.JavascriptAnchorActionGeneratorConfig;
import com.google.testing.webtestingexplorer.config.MaxRepeatedActionSequenceFilter;
import com.google.testing.webtestingexplorer.config.MultiCriterionActionGeneratorConfig;
import com.google.testing.webtestingexplorer.config.WebTestingConfig;
import com.google.testing.webtestingexplorer.driver.FirefoxWebDriverFactory;
import com.google.testing.webtestingexplorer.explorer.WebTestingExplorer;
import com.google.testing.webtestingexplorer.identifiers.WebElementWithIdentifier;
import com.google.testing.webtestingexplorer.state.CountOfElementsStateChecker;
import com.google.testing.webtestingexplorer.testcase.TestCaseWriter;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.util.List;
import java.util.Set;

/**
 * Entry point for the application that tries things out.
 * 
 * @author smcmaster@google.com (Scott McMaster)
 */
public class WebTestingExplorerMain {

  @Option(name="-useElementsCache", usage="Whether or not to try your luck with our "+
      "WebDriver Elements cache")
  private boolean useElementsCache = false;
  
  @Option(name="-outputDir", usage="Directory to save test cases in")
  private String outputDir = "/tmp/webtestexplorer";
  
  @Argument
  private List<String> arguments = Lists.newArrayList();
  
  private void run(String[] args) throws Exception {
    CmdLineParser parser = new CmdLineParser(this);
    parser.parseArgument(args);
    String url = arguments.get(0);
    
    WebTestingConfig config = new WebTestingConfig()
        .setTestCaseWriter(new TestCaseWriter(outputDir))
        .setUrl(url)
        .setMaxLength(5)
        .addStateChecker(new CountOfElementsStateChecker())
        .setOracleConfigFactory(new SampleOracleConfigFactory())
        .setWebDriverFactory(new FirefoxWebDriverFactory(true))
        .withRefreshButtonAction()
        .addActionSequenceFilter(new MaxRepeatedActionSequenceFilter(2))
        .addActionGeneratorConfig(new MultiCriterionActionGeneratorConfig(
            null, null, ".*email.*", null) {          
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

  public static void main(String[] args) throws Exception {
    new WebTestingExplorerMain().run(args);
  }
}
