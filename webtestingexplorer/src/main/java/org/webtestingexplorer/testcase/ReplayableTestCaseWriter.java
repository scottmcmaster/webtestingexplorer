package org.webtestingexplorer.testcase;

import com.thoughtworks.xstream.XStream;

import org.webtestingexplorer.driver.ActionSequenceRunner.ActionSequenceResult;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Outputs test cases to a files in a specific directory in our replayable XML format.
 * 
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
public class ReplayableTestCaseWriter extends AbstractTestCaseWriter {

  private final static Logger LOGGER =
      Logger.getLogger(ReplayableTestCaseWriter.class.getName());

  private String outputDirectory;
  private XStream xstream;
  
  /**
   * @param outputDirectory
   */
  public ReplayableTestCaseWriter(String outputDirectory) {
    xstream = TestCaseXStream.createXStream();
    new File(outputDirectory).mkdirs();
    this.outputDirectory = outputDirectory;
  }

  /**
   * Writes a test case based on the given action sequence.
   */
  @Override
  public void writeTestCase(TestCase testCase, int testCaseNumber, ActionSequenceResult result) {
    String fileName = "test-" + testCaseNumber + ".xml";
    String xml = xstream.toXML(testCase);
    String fullPath = outputDirectory + "/" + fileName;
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
