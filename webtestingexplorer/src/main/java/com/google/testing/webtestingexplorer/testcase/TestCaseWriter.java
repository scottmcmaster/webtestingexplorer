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

import com.thoughtworks.xstream.XStream;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Outputs test cases to a files in a specific directory.
 * 
 * @author smcmaster@google.com (Scott McMaster)
 */
public class TestCaseWriter {

  private final static Logger LOGGER =
      Logger.getLogger(TestCaseWriter.class.getName());

  private String outputDirectory;
  private XStream xstream;
  
  /**
   * @param outputDirectory
   */
  public TestCaseWriter(String outputDirectory) {
    xstream = TestCaseXStream.createXStream();
    this.outputDirectory = outputDirectory;
  }

  /**
   * Writes a test case based on the given action sequence.
   */
  public void writeTestCase(TestCase testCase, String filename) {
    String xml = xstream.toXML(testCase);
    String fullPath = outputDirectory + "/" + filename;
    LOGGER.log(Level.INFO, "Writing test case to " + fullPath);
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
