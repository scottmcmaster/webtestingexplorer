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
package com.google.testing.webtestingexplorer.actions;

import com.google.testing.webtestingexplorer.driver.WebDriverWrapper;

/**
 * Hits the browser "forward" button. Due to the way that WebDriver is implemented,
 * if the button is not enabled, this is a no-op.
 * 
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
public class ForwardAction extends Action {

  @Override
  public void perform(WebDriverWrapper driver) {
    super.perform(driver);
    driver.getDriver().navigate().forward();
  }

  @Override
  public String toString() {
    return "Browser: Forward()";
  }
}
