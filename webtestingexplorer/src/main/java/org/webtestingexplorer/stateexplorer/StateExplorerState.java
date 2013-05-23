package org.webtestingexplorer.stateexplorer;

import com.google.common.collect.Sets;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.webtestingexplorer.actions.Action;
import org.webtestingexplorer.state.State;

import java.util.Collection;
import java.util.Set;

/**
 * Captures the full state from the state checkers, plus all the available actions
 * at a given time. This ends up being a node in our complete state exploration graph.
 */
public class StateExplorerState {

  private Set<State> states;
  private Set<Action> actions;
  private Set<StateExplorerState> followingStates;
  
  public StateExplorerState(Collection<State> states, Collection<Action> actions) {
    this.states = Sets.newHashSet(states);
    this.actions = Sets.newHashSet(actions);
    followingStates = Sets.newHashSet();
  }
  
  public Set<StateExplorerState> getFollowingStates() {
    return followingStates;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj.getClass() != this.getClass()) {
      return false;
    }
    StateExplorerState other = (StateExplorerState) obj;
    return new EqualsBuilder()
        .append(states, other.states)
        .append(actions, other.actions).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder()
        .append(states).append(actions).hashCode();
  }

  public void addFollowingState(StateExplorerState newState) {
    followingStates.add(newState);
  }
}
