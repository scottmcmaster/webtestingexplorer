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

import org.webtestingexplorer.driver.WebDriverWrapper;

import java.util.List;

/**
 * A component that evaluates the state of the web application and
 * detects failure conditions, returned as a list of {@link FailureReason}s.
 * 
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
public interface Oracle {
	
  /**
   * Tells the oracle to reset any internal state it keeps. This will be called before each step for
   * an after-action oracle, and before the beginning of a test case for a final oracle.
   */
  void reset();

  /**
   * Checks for failure conditions in the web application.
   * 
   * @param driver the web driver from where we can get the state we
   *    need to do the checking.
   * @return a list of failure reasons, which may be empty OR null if no failures
   *    are detected.
   */
  List<FailureReason> check(WebDriverWrapper driver);
}
