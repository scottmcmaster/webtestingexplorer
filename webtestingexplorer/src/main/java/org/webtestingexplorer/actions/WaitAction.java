package org.webtestingexplorer.actions;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.webtestingexplorer.driver.WebDriverWrapper;

/**
 * Action that sleeps the webtestingexplorer client for a specific period of time.
 * 
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
public class WaitAction extends Action {

  private final long millis;

  /**
   * Creates a wait action that will wait for the specified number of milliseconds.
   */
  public WaitAction(long millis) {
    this.millis = millis;
  }
  
  @Override
  public void perform(WebDriverWrapper driver) {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException useless) {
    }
  }
  
  @Override
  public String toString() {
    return "Wait: " + millis + " millis";
  }
  
  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof WaitAction)) {
      return false;
    }
    WaitAction other = (WaitAction) obj;
    return new EqualsBuilder().appendSuper(super.equals(obj))
        .append(millis, other.millis).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().appendSuper(super.hashCode()).append(millis).hashCode();
  }

  public long getMillis() {
    return millis;
  }
}
