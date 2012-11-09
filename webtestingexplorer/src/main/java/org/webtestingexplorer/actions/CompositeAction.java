package org.webtestingexplorer.actions;

import com.google.common.collect.ImmutableList;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.webtestingexplorer.driver.WebDriverWrapper;

import java.util.Arrays;
import java.util.List;

/**
 * Rolls two or more actions into one, ensuring that they will always be executed
 * together and in order, but treated as a single "action" in the number-of-actions
 * count.
 * 
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
public class CompositeAction extends Action {

  private final List<Action> actions;
  
  public CompositeAction(Action... actionsInOrder) {
    actions = Arrays.asList(actionsInOrder);
  }
  
  public List<Action> getActions() {
    return ImmutableList.copyOf(actions);
  }
  
  @Override
  public void perform(WebDriverWrapper driver) {
    for (Action action : actions) {
      action.perform(driver);
    }
  }
  
  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof CompositeAction)) {
      return false;
    }
    CompositeAction other = (CompositeAction) obj;
    return new EqualsBuilder().appendSuper(super.equals(obj))
        .append(actions, other.actions).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().appendSuper(super.hashCode()).append(actions).hashCode();
  }
  
  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder("Composite: " );
    for (Action action : actions) {
      builder.append("\n  ");
      builder.append(action);
    }
    return builder.toString();
  }
}
