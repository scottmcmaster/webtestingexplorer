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
package com.google.testing.webtestingexplorer.testcase;

import com.google.testing.webtestingexplorer.actions.Action;
import com.google.testing.webtestingexplorer.actions.ActionSequence;
import com.google.testing.webtestingexplorer.actions.BackAction;
import com.google.testing.webtestingexplorer.actions.ClickAction;
import com.google.testing.webtestingexplorer.actions.ForwardAction;
import com.google.testing.webtestingexplorer.actions.RefreshAction;
import com.google.testing.webtestingexplorer.actions.SelectAction;
import com.google.testing.webtestingexplorer.actions.SetTextAction;
import com.google.testing.webtestingexplorer.identifiers.IdWebElementIdentifier;
import com.google.testing.webtestingexplorer.identifiers.IndexWebElementIdentifier;
import com.google.testing.webtestingexplorer.identifiers.NameWebElementIdentifier;
import com.google.testing.webtestingexplorer.identifiers.WebElementIdentifier;
import com.google.testing.webtestingexplorer.identifiers.XpathWebElementIdentifier;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

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
    xstream.alias("ActionSequence", ActionSequence.class);
    xstream.alias("Action", Action.class);
    xstream.alias("BackAction", BackAction.class);
    xstream.alias("ClickAction", ClickAction.class);
    xstream.alias("ForwardAction", ForwardAction.class);
    xstream.alias("RefreshAction", RefreshAction.class);
    xstream.alias("SelectAction", SelectAction.class);
    xstream.alias("SetTextAction", SetTextAction.class);
    xstream.alias("WebElementIdentifier", WebElementIdentifier.class);
    xstream.alias("NameWebElementIdentifier", NameWebElementIdentifier.class);
    xstream.alias("IdWebElementIdentifier", IdWebElementIdentifier.class);
    xstream.alias("IndexWebElementIdentifier", IndexWebElementIdentifier.class);
    xstream.alias("XpathWebElementIdentifier", XpathWebElementIdentifier.class);
    xstream.alias("TestCase", TestCase.class);
    return xstream;
  }
}
