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

import org.webtestingexplorer.actions.Action;
import org.webtestingexplorer.actions.ActionSequence;
import org.webtestingexplorer.actions.BackAction;
import org.webtestingexplorer.actions.ClickAction;
import org.webtestingexplorer.actions.ForwardAction;
import org.webtestingexplorer.actions.RefreshAction;
import org.webtestingexplorer.actions.SelectAction;
import org.webtestingexplorer.actions.SetTextAction;
import org.webtestingexplorer.identifiers.AttributeValueWebElementIdentifier;
import org.webtestingexplorer.identifiers.IdWebElementIdentifier;
import org.webtestingexplorer.identifiers.IndexWebElementIdentifier;
import org.webtestingexplorer.identifiers.NameWebElementIdentifier;
import org.webtestingexplorer.identifiers.WebElementIdentifier;
import org.webtestingexplorer.identifiers.XpathWebElementIdentifier;
import org.webtestingexplorer.state.CountOfElementsState;
import org.webtestingexplorer.state.CustomizedPropertiesElementsState;
import org.webtestingexplorer.state.ElementsState;
import org.webtestingexplorer.state.SelectedElementsState;
import org.webtestingexplorer.state.VisibleElementsState;

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
    xstream.alias("CountOfElementsState", CountOfElementsState.class);
    xstream.alias("CustomizedPropertiesElementsState", CustomizedPropertiesElementsState.class);
    xstream.alias("ElementsState", ElementsState.class);
    xstream.alias("VisibleElementsState", VisibleElementsState.class);
    xstream.alias("TestCase", TestCase.class);
    xstream.alias("AttributeValueWebElementIdentifier", AttributeValueWebElementIdentifier.class);
    xstream.alias("SelectedElementsState", SelectedElementsState.class);
    return xstream;
  }
}
