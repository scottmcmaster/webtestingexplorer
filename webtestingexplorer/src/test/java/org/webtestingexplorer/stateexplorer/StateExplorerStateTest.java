package org.webtestingexplorer.stateexplorer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Tests for the {@link StateExplorerState} class.
 */
public class StateExplorerStateTest extends StateExplorerTestCase {

  @Test
  public void equalsTest() {    
    assertTrue(first.equals(second));
    assertTrue(second.equals(first));
    assertFalse(first.equals(third));
    assertFalse(third.equals(first));
  }
  
  @Test
  public void addFollowingState() {
    assertTrue(first.getFollowingStates().isEmpty());
    first.addFollowingState(second);
    assertEquals(1, first.getFollowingStates().size());
    first.addFollowingState(third);
    assertEquals(2, first.getFollowingStates().size());
    first.addFollowingState(second);
    assertEquals(2, first.getFollowingStates().size());
  }
}
