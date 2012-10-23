package org.webtestingexplorer.testcase;

/**
 * Base class for {@link TestCaseWriter}s that support some useful features. 
 * 
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
public abstract class AbstractTestCaseWriter implements TestCaseWriter {

  /**
   * Indicates whether or not to only write out the test case if a failure has
   * been detected by the explorer.
   */
  protected boolean writeOnFailureOnly;

  @Override
  public boolean isWriteOnFailureOnly() {
    return writeOnFailureOnly;
  }

  public void setWriteOnFailureOnly(boolean isWriteOnFailureOnly) {
    this.writeOnFailureOnly = isWriteOnFailureOnly;
  }
}
