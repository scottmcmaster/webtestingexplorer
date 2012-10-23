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
public class ReplayableTestCaseReader {

  private final static Logger LOGGER =
      Logger.getLogger(ReplayableTestCaseReader.class.getName());

  private XStream xstream;
  
  public ReplayableTestCaseReader() {
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
