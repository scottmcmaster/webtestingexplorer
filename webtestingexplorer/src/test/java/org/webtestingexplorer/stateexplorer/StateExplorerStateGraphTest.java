package org.webtestingexplorer.stateexplorer;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

/**
 * Tests for the {@link StateExplorerStateGraph} class.
 */
public class StateExplorerStateGraphTest extends StateExplorerTestCase {

  @Test
  public void findState() {
    StateExplorerStateGraph graph = new StateExplorerStateGraph();
    assertNull(graph.getRootState());
    
    StateExplorerState foundState = graph.findState(first);
    assertSame(first, graph.getRootState());
    assertSame(first, foundState);
    
    foundState = graph.findState(second);
    assertSame(first, foundState);
    
    foundState = graph.findState(second);
    assertSame(first, foundState);
    assertSame(first, graph.getRootState());
    
    foundState = graph.findState(third);
    assertSame(third, foundState);
    assertSame(first, graph.getRootState());
  }
}
