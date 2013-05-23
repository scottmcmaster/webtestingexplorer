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
package org.webtestingexplorer.testcase;


import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

import org.webtestingexplorer.actions.ActionSequenceXStream;
import org.webtestingexplorer.config.WebElementSelectorRegistry;
import org.webtestingexplorer.config.selector.ClassWebElementSelector;
import org.webtestingexplorer.config.selector.CompositeWebElementSelector;
import org.webtestingexplorer.config.selector.CssWebElementSelector;
import org.webtestingexplorer.config.selector.FilterWebElementSelector;
import org.webtestingexplorer.config.selector.TagWebElementSelector;
import org.webtestingexplorer.config.selector.XpathWebElementSelector;
import org.webtestingexplorer.state.StateXStream;

/**
 * XStream configuration for reading and writing test cases.
 * 
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
public class TestCaseXStream {

  /**
   * Creates a correctly-configured {@link XStream} instance.
   */
  public static XStream createXStream() {
    XStream xstream = new XStream(new StaxDriver());
    ActionSequenceXStream.addActionSequenceAliases(xstream);
    StateXStream.addStateAliases(xstream);
    xstream.alias("TestCase", TestCase.class);
    xstream.alias("TestCaseConfig", TestCaseConfig.class);
    xstream.alias("WebElementSelectorRegistry", WebElementSelectorRegistry.class);
    xstream.alias("TagWebElementSelector", TagWebElementSelector.class);
    xstream.alias("CssWebElementSelector", CssWebElementSelector.class);
    xstream.alias("XpathWebElementSelector", XpathWebElementSelector.class);
    xstream.alias("FilterWebElementSelector", FilterWebElementSelector.class);
    xstream.alias("ClassWebElementSelector", ClassWebElementSelector.class);
    xstream.alias("CompositeWebElementSelector", CompositeWebElementSelector.class);
    
    // TODO(smcmaster): I think we need to expose some API here so that users can
    // register their custom classes to be serialized nicely in the test case format.
    return xstream;
  }
}
