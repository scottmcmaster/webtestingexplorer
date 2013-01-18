package org.webtestingexplorer.testcase;

import org.webtestingexplorer.driver.ActionSequenceRunner.ActionSequenceResult;

/**
 * Writes out test cases.
 * 
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
public interface TestCaseWriter {
  /**
   * Writes out the test case with the given unique id.
   */
  void writeTestCase(TestCase testCase, String testCaseId, ActionSequenceResult result);
  
  /**
   * Flags whether to only invoke this writer in the case where a failure has
   * been detected.
   */
  boolean isWriteOnFailureOnly();
}
