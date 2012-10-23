package org.webtestingexplorer.testcase;

import org.webtestingexplorer.actions.Action;
import org.webtestingexplorer.driver.ActionSequenceRunner.ActionSequenceResult;
import org.webtestingexplorer.oracles.FailureReason;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Outputs test cases in a nice readable format better suited for manual re-execution.
 * 
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
public class PrettyTestCaseWriter extends AbstractTestCaseWriter {

  private final static Logger LOGGER =
      Logger.getLogger(PrettyTestCaseWriter.class.getName());

  private String outputDirectory;
  
  public PrettyTestCaseWriter(String outputDirectory, boolean writeOnFailureOnly) {
    this.outputDirectory = outputDirectory;
    this.writeOnFailureOnly = writeOnFailureOnly;
  }

  public PrettyTestCaseWriter(String outputDirectory) {
    this(outputDirectory, false);
  }

  /**
   * Writes a test case based on the given action sequence.
   */
  @Override
  public void writeTestCase(TestCase testCase, int testCaseNumber, ActionSequenceResult result) {
    String fileName = "test-" + testCaseNumber + ".txt";
    String fullPath = outputDirectory + "/" + fileName;
    LOGGER.log(Level.INFO, "Writing test case to " + fullPath);
    BufferedWriter out = null;
    try {
      out = new BufferedWriter(new FileWriter(new File(fullPath)));
      int stepNum = 1;
      for (Action action : testCase.getActionSequence().getActions()) {
        // TODO(smcmaster): This still isn't very "pretty". The identifier strings in particular
        // are rather ugly. Probably we should add special pretty-toString methods to
        // each of the actions/identifiers.
        String line = "" + stepNum + ". " + action.toString();
        out.write(line);
        out.newLine();
        ++stepNum;
      }
      out.newLine();
      
      if (!result.hasFailures()) {
        out.write("PASSED");
      } else {
        out.write("FAILED");
        for (FailureReason reason : result.getFailures()) {
          out.write(reason.getMessage());
          out.newLine();
        }
      }
      out.newLine();
    } catch (Exception e) {
      System.err.println("Failed to write " + fullPath);
      e.printStackTrace();
    }
    finally {
      try { out.close(); } catch (Exception e) {}
    }
  }
}
