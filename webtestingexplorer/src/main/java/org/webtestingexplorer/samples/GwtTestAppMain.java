package org.webtestingexplorer.samples;

import java.util.List;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.webtestingexplorer.config.OracleConfig;
import org.webtestingexplorer.config.OracleConfigFactory;
import org.webtestingexplorer.config.WebElementSelectorRegistry;
import org.webtestingexplorer.config.WebTestingConfig;
import org.webtestingexplorer.config.actiongenerator.JavascriptAnchorActionGeneratorConfig;
import org.webtestingexplorer.config.filter.MaxRepeatedActionSequenceFilter;
import org.webtestingexplorer.config.selector.TagWebElementSelector;
import org.webtestingexplorer.driver.FirefoxWebDriverFactory;
import org.webtestingexplorer.explorer.WebTestingExplorer;
import org.webtestingexplorer.oracles.ServerCollectingLoggingOracle;
import org.webtestingexplorer.state.CountOfElementsStateChecker;
import org.webtestingexplorer.testcase.ReplayableTestCaseWriter;

import com.google.common.collect.Lists;

/**
 * Entry point for the application that tries things out.
 * Example arg: http://127.0.0.1:8888/WebTestingExplorerGwtTestApp.html
 * 
 * @author smcmaster@google.com (Scott McMaster)
 */
public class GwtTestAppMain {

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
        .addTestCaseWriter(new ReplayableTestCaseWriter(outputDir))
        .setUrl(url)
        .setMaxLength(5)
        .addStateChecker(new CountOfElementsStateChecker())
        .setOracleConfigFactory(new OracleConfigFactory() {
					@Override
					public OracleConfig createOracleConfig() {
				    OracleConfig oracleConfig = new OracleConfig()
		        	.addAfterActionOracle(new ServerCollectingLoggingOracle("http://localhost:8888/logmessages"));
				    return oracleConfig;
					}
				})
        .setWebDriverFactory(new FirefoxWebDriverFactory(true))
        .withRefreshButtonAction()
        .addActionSequenceFilter(new MaxRepeatedActionSequenceFilter(2))
        .addActionGeneratorConfig(new JavascriptAnchorActionGeneratorConfig());
    new WebTestingExplorer(config).run();    
  }

  public static void main(String[] args) throws Exception {
    new GwtTestAppMain().run(args);
  }
}
