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

package org.webtestingexplorer.oracles;

import com.google.common.collect.Lists;

import net.jsourcerer.webdriver.jserrorcollector.JavaScriptError;

import org.openqa.selenium.firefox.FirefoxDriver;
import org.webtestingexplorer.driver.WebDriverWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Looks for Javascript errors using JSErrorCollector.
 * @see "http://mguillem.wordpress.com/2011/10/11/webdriver-capture-js-errors-while-running-tests/."
 * 
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
public class JSErrorCollectorOracle implements Oracle {

  private final static Logger LOGGER = Logger.getLogger(JSErrorCollectorOracle.class.getName());

  @Override
  public List<FailureReason> check(WebDriverWrapper driver) {
    if (!(driver.getDriver() instanceof FirefoxDriver)) {
      LOGGER.warning("JSErrorCollectorOracle requires FirefoxDriver, skipping checks");
      return Lists.newArrayList();
    }
    
    List<FailureReason> result = new ArrayList<FailureReason>();
    List<JavaScriptError> jsErrors = JavaScriptError.readErrors(driver.getDriver());
    for (JavaScriptError jsError : jsErrors) {
      result.add(createFailureReason(jsError));
    }
    return result;
  }

  /**
   * Formats a {@link JavaScriptError} as a single string suitable for our
   * error-reporting framework.
   */
  private FailureReason createFailureReason(JavaScriptError jsError) {
    String message = jsError.getSourceName() + " line " + jsError.getLineNumber() +
        ": " + jsError.getErrorMessage();
    return new FailureReason(message);
  }
}
