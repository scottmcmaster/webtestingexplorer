// Copyright 2011 Google Inc. All Rights Reserved.

package com.google.testing.webtestingexplorer.oracles;

import com.google.testing.webtestingexplorer.driver.WebDriverWrapper;

import net.jsourcerer.webdriver.jserrorcollector.JavaScriptError;

import java.util.ArrayList;
import java.util.List;

/**
 * Looks for Javascript errors using JSErrorCollector.
 * @see "http://mguillem.wordpress.com/2011/10/11/webdriver-capture-js-errors-while-running-tests/."
 * 
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
public class JSErrorCollectorOracle implements Oracle {

  @Override
  public List<FailureReason> check(WebDriverWrapper driver) {
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
