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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.logging.Logger;

/**
 * Reads in a test case from an input source.
 * 
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
public class TestCaseReader {

  private final static Logger LOGGER =
      Logger.getLogger(TestCaseReader.class.getName());

  private XStream xstream;
  
  public TestCaseReader() {
    xstream = TestCaseXStream.createXStream();
  }

  /**
   * Reads in a test case from the given file system path.
   */
  public TestCase readTestCase(String path) throws FileNotFoundException {
    LOGGER.info("Reading " + path);
    File inputFile = new File(path);
    FileInputStream inputStream = null;
    try {
      inputStream = new FileInputStream(inputFile);
      return (TestCase) xstream.fromXML(inputStream);
    } finally {
      if (inputStream != null) {
        try { inputStream.close(); } catch (Exception e) {}      }
    }
  }
}
