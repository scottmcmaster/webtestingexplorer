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
import com.google.testing.webtestingexplorer.actions.ClickAction;
import com.google.testing.webtestingexplorer.actions.SetTextAction;
import com.google.testing.webtestingexplorer.identifiers.IdWebElementIdentifier;
import com.google.testing.webtestingexplorer.identifiers.IndexWebElementIdentifier;
import com.google.testing.webtestingexplorer.identifiers.NameWebElementIdentifier;
import com.google.testing.webtestingexplorer.identifiers.WebElementIdentifier;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * Outputs test cases to a files in a specific directory.
 * 
 * @author smcmaster@google.com (Scott McMaster)
 */
public class TestCaseWriter {

  private String outputDirectory;
  private XStream xstream;
  
  /**
   * @param outputDirectory
   */
  public TestCaseWriter(String outputDirectory) {
    xstream = new XStream(new StaxDriver());
    xstream.alias("ActionSequence", ActionSequence.class);
    xstream.alias("Action", Action.class);
    xstream.alias("ClickAction", ClickAction.class);
    xstream.alias("SetTextAction", SetTextAction.class);
    xstream.alias("WebElementIdentifier", WebElementIdentifier.class);
    xstream.alias("NameWebElementIdentifier", NameWebElementIdentifier.class);
    xstream.alias("IdWebElementIdentifier", IdWebElementIdentifier.class);
    xstream.alias("IndexWebElementIdentifier", IndexWebElementIdentifier.class);

    this.outputDirectory = outputDirectory;
  }

  /**
   * Writes a test case based on the given action sequence.
   */
  public void writeTestCase(ActionSequence actionSequence, String filename) {
    String xml = xstream.toXML(actionSequence);
    System.out.println(xml);
    String fullPath = outputDirectory + "/" + filename;
    Writer out = null;
    try {
      out = new OutputStreamWriter(new FileOutputStream(fullPath));
      out.write(xml);
    } catch (Exception e) {
      System.err.println("Failed to write " + fullPath);
      e.printStackTrace();
    }
    finally {
      try { out.close(); } catch (Exception e) {}
    }
  }

}
