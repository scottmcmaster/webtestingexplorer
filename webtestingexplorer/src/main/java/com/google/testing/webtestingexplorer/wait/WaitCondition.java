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
package com.google.testing.webtestingexplorer.wait;

import com.google.testing.webtestingexplorer.driver.WebDriverWrapper;

/**
 * For adding wait checks to the explorer/replayer.
 * 
 * @author smcmaster@google.com (Scott McMaster)
 */
public interface WaitCondition {
  
  /**
   * Called before a wait to give the WaitCondition a chance to preserve
   * any initial state.
   */
  void reset();
  
  /**
   * @return true if it is ok to proceed, false to keep waiting.
   */
  boolean canContinue(WebDriverWrapper driver);
  
  /**
   * @return a description of the wait condition, which will get logged and be
   *     unbelievably useful for debugging.
   */
  String getDescription();
}
