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
import org.webtestingexplorer.config.WebElementSelectorRegistry;
import org.webtestingexplorer.config.WebTestingConfig;
import org.webtestingexplorer.config.actiongenerator.JavascriptAnchorActionGeneratorConfig;
import org.webtestingexplorer.config.actiongenerator.MultiCriterionActionGeneratorConfig;
import org.webtestingexplorer.config.filter.MaxRepeatedActionSequenceFilter;
import org.webtestingexplorer.config.filter.OrderInsensitiveActionSequenceFilter;
import org.webtestingexplorer.config.selector.TagWebElementSelector;
import org.webtestingexplorer.driver.ChromeWebDriverFactory;
import org.webtestingexplorer.driver.FirefoxWebDriverFactory;
import org.webtestingexplorer.driver.WebDriverWrapper;
import org.webtestingexplorer.explorer.WebTestingExplorer;
import org.webtestingexplorer.identifiers.NameWebElementIdentifier;
import org.webtestingexplorer.identifiers.WebElementWithIdentifier;
import org.webtestingexplorer.javascript.JavaScriptUtil;
import org.webtestingexplorer.state.CountOfElementsStateChecker;
import org.webtestingexplorer.testcase.ReplayableTestCaseWriter;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;
import java.util.logging.LogManager;

/**
 * Entry point for the application that tries things out.
 * 
 * @author smcmaster@google.com (Scott McMaster)
 */
public class FeedbackFormMain {

  @Option(name="-useElementsCache", usage="Whether or not to try your luck with our "+
      "WebDriver Elements cache")
  private boolean useElementsCache = false;
  
  @Option(name="-outputDir", usage="Directory to save test cases in")
  private String outputDir = "/tmp/webtestexplorer";
  
  @Option(name="-queueFile", usage="Filename to read/write the persistent queue of action sequences (may be empty)")
  private String queueFile = "";
  
  @Argument
  private List<String> arguments = Lists.newArrayList();
  
  private void run(String[] args) throws Exception {
    CmdLineParser parser = new CmdLineParser(this);
    parser.parseArgument(args);
    String url = arguments.get(0);
    
    WebElementSelectorRegistry.getInstance().registerActionable(
        new TagWebElementSelector("input", "textarea", "a", "button", "select"));
    
    WebTestingConfig config = new WebTestingConfig()
        .addTestCaseWriter(new ReplayableTestCaseWriter(outputDir))
        .setQueueFilename(queueFile)
        .setUrl(url)
        .setMaxLength(5)
        .addStateChecker(new CountOfElementsStateChecker())
        .setOracleConfigFactory(new SampleOracleConfigFactory())
        .setWebDriverFactory(new FirefoxWebDriverFactory(true))
        //.setWebDriverFactory(new ChromeWebDriverFactory())
        .withRefreshButtonAction()
        .addActionSequenceFilter(new MaxRepeatedActionSequenceFilter(2))
        .addActionSequenceFilter(createNoTextboxOrderingActionSequenceFilter())
        .addActionGeneratorConfig(new MultiCriterionActionGeneratorConfig(
            null, null, ".*email.*", null, null) {          
          @Override
          public Set<Action> generateActions(WebDriverWrapper driver,
              WebElementWithIdentifier elementWithId) {
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

  private static void setupLogging() {
    // Work around annoying browsermob issue
    // https://github.com/webmetrics/browsermob-proxy/issues/77
    new org.browsermob.proxy.util.Log();
    
    InputStream in = null;
    try {
        in = JavaScriptUtil.class.getResourceAsStream("/logging.properties");
        LogManager.getLogManager().readConfiguration(in);
    } catch (IOException ex) {
        System.err.println("WARNING: Logging not configured (console output only)");
    } finally {
      try { in.close(); } catch (Exception e) {}
    }
  }
  
  public static void main(String[] args) throws Exception {
    setupLogging();
    new FeedbackFormMain().run(args);
  }
}
