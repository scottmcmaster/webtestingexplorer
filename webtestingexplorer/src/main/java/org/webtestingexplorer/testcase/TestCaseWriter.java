package org.webtestingexplorer.testcase;

import org.webtestingexplorer.driver.ActionSequenceRunner.ActionSequenceResult;

/**
 * Writes out test cases.
 * 
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
public interface TestCaseWriter {
  /**
   * Writes out the test case with the given ordinal number which will be
   * unique across an explorer run.
   */
  void writeTestCase(TestCase testCase, int testCaseNumber, ActionSequenceResult result);
  
  /**
   * Flags whether to only invoke this writer in the case where a failure has
   * been detected.
   */
  boolean isWriteOnFailureOnly();
}
