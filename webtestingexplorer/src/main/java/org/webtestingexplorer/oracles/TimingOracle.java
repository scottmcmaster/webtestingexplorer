package org.webtestingexplorer.oracles;

import com.google.common.collect.ImmutableList;

import org.webtestingexplorer.driver.WebDriverWrapper;

import java.util.List;

/**
 * Oracle that logs a failure if an action or test case exceeds a given amount
 * of time (in milliseconds). Note that this should not be used for "exact" timings
 * because (among other things) we can't be sure how much time will be spent
 * checking other oracles before we get to this one.
 * 
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
public class TimingOracle implements Oracle {

  private final long maxAllowableMillis;
  private long startMillis;
  
  public TimingOracle(long maxAllowableMillis) {
    this.maxAllowableMillis = maxAllowableMillis;
  }
  
  @Override
  public void reset() {
    startMillis = System.currentTimeMillis();
  }

  @Override
  public List<FailureReason> check(WebDriverWrapper driver) {
    long endMillis = System.currentTimeMillis();
    if (endMillis - startMillis > maxAllowableMillis) {
      FailureReason reason = new FailureReason("Deadline of " + maxAllowableMillis +
          "millis exceeded");
      return ImmutableList.of(reason);
    }
    return null;
  }
}
