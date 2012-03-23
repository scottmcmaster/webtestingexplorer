/*
Copyright 2012 Google Inc. All Rights Reserved.

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

import com.google.testing.webtestingexplorer.config.ActionableWebElementSelectorFactory;
import com.google.testing.webtestingexplorer.config.TagWebElementSelector;
import com.google.testing.webtestingexplorer.config.WebElementSelector;

/**
 * Example actionable web element factory. Note that we don't do this as an inner
 * class because the replayer needs to be able to access it at runtime.
 * 
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
public class SampleActionableWebElementSelectorFactory
    implements ActionableWebElementSelectorFactory {

  @Override
  public WebElementSelector createActionableWebElementSelector() {
    return new TagWebElementSelector("input", "textarea", "a", "button", "select");
  }
}
