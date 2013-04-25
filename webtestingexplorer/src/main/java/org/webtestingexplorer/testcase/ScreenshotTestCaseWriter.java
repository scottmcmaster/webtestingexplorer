package org.webtestingexplorer.testcase;

import org.apache.commons.io.FileUtils;
import org.webtestingexplorer.driver.ActionSequenceRunner.ActionSequenceResult;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Outputs screenshots collected during action sequence execution. Note that
 * you will get an error if you use this and you DID NOT configure the explorer
 * to capture screenshots in the first place using
 * {@link org.webtestingexplorer.config.WebTestingConfig#setCaptureScreenshots}.
 * 
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
public class ScreenshotTestCaseWriter extends AbstractTestCaseWriter {

  private final static Logger LOGGER =
      Logger.getLogger(ScreenshotTestCaseWriter.class.getName());

  private String outputDirectory;
  
  public ScreenshotTestCaseWriter(String outputDirectory, boolean writeOnFailureOnly) {
    this.outputDirectory = outputDirectory;
    this.writeOnFailureOnly = writeOnFailureOnly;
  }

  public ScreenshotTestCaseWriter(String outputDirectory) {
    this(outputDirectory, false);
  }

  /**
   * Writes a the screenshots for the given action sequence.
   */
  @Override
  public void writeTestCase(TestCase testCase, String testCaseId, ActionSequenceResult result) {
    if (result.getScreenshots() == null) {
      throw new IllegalStateException("You need to run with WebTestingConfig.setCaptureScreenshots(true)");
    }
    
    String screenshotDirName = "screenshots-" + testCaseId;
    String screenshotDirPath = outputDirectory + "/" + screenshotDirName;
    int i = 1;
    LOGGER.log(Level.INFO, "Writing screenshots to " + screenshotDirPath);
    for (Byte[] screenshotBytes : result.getScreenshots()) {
      String fullPath = screenshotDirPath + '/' + i++ + ".png";
      byte[] bytes = new byte[screenshotBytes.length];
      for (int byteNum = 0; byteNum < screenshotBytes.length; ++byteNum) {
        bytes[byteNum]  = screenshotBytes[byteNum];
      }
      try {
        FileUtils.writeByteArrayToFile(new File(fullPath), bytes);
      } catch (FileNotFoundException e) {
        LOGGER.log(Level.SEVERE, "File not found: " + fullPath, e);
      } catch (IOException e) {
        LOGGER.log(Level.SEVERE, "Error writing: " + fullPath, e);
      }
    }
  }
}
