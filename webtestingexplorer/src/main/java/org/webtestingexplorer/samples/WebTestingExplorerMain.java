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
package org.webtestingexplorer.samples;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.webtestingexplorer.actions.Action;
import org.webtestingexplorer.actions.ActionGenerator;
import org.webtestingexplorer.actions.SetTextAction;
import org.webtestingexplorer.config.ActionSequenceFilter;
import org.webtestingexplorer.config.JavascriptAnchorActionGeneratorConfig;
import org.webtestingexplorer.config.MaxRepeatedActionSequenceFilter;
import org.webtestingexplorer.config.MultiCriterionActionGeneratorConfig;
import org.webtestingexplorer.config.OrderInsensitiveActionSequenceFilter;
import org.webtestingexplorer.config.TagWebElementSelector;
import org.webtestingexplorer.config.WebElementSelectorRegistry;
import org.webtestingexplorer.config.WebTestingConfig;
import org.webtestingexplorer.driver.FirefoxWebDriverFactory;
import org.webtestingexplorer.explorer.WebTestingExplorer;
import org.webtestingexplorer.identifiers.NameWebElementIdentifier;
import org.webtestingexplorer.identifiers.WebElementWithIdentifier;
import org.webtestingexplorer.state.CountOfElementsStateChecker;
import org.webtestingexplorer.testcase.TestCaseWriter;

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
    
    WebElementSelectorRegistry.getInstance().registerActionable(
        new TagWebElementSelector("input", "textarea", "a", "button", "select"));
    
    WebTestingConfig config = new WebTestingConfig()
        .setTestCaseWriter(new TestCaseWriter(outputDir))
        .setUrl(url)
        .setMaxLength(5)
        .addStateChecker(new CountOfElementsStateChecker())
        .setOracleConfigFactory(new SampleOracleConfigFactory())
        .setWebDriverFactory(new FirefoxWebDriverFactory(true))
        .withRefreshButtonAction()
        .addActionSequenceFilter(new MaxRepeatedActionSequenceFilter(2))
        .addActionSequenceFilter(createNoTextboxOrderingActionSequenceFilter())
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

  private ActionSequenceFilter createNoTextboxOrderingActionSequenceFilter() {
    Set<Action> actions = Sets.newHashSet();
    actions.addAll(ActionGenerator.createDefaultTextWidgetActions(
        new NameWebElementIdentifier("feedback_name")));
    actions.addAll(ActionGenerator.createDefaultTextWidgetActions(
        new NameWebElementIdentifier("feedback_affiliation")));
    actions.addAll(ActionGenerator.createDefaultTextWidgetActions(
        new NameWebElementIdentifier("feedback_email")));
    actions.addAll(ActionGenerator.createDefaultTextWidgetActions(
        new NameWebElementIdentifier("feedback_comments")));
    actions.addAll(ActionGenerator.createDefaultSelectWidgetActions(
        new NameWebElementIdentifier("feedback_category"), 2));
    return new OrderInsensitiveActionSequenceFilter(actions);
  }

  public static void main(String[] args) throws Exception {
    new WebTestingExplorerMain().run(args);
  }
}
