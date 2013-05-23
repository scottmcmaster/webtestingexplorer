package org.webtestingexplorer.stateexplorer;


import com.google.common.collect.Lists;

import org.junit.Before;
import org.webtestingexplorer.actions.Action;
import org.webtestingexplorer.actions.ClickAction;
import org.webtestingexplorer.identifiers.IdWebElementIdentifier;
import org.webtestingexplorer.state.CountOfElementsState;
import org.webtestingexplorer.state.State;

import java.util.Collection;

/**
 * Helper base class for state explorer tests.
 */
public class StateExplorerTestCase {

  protected StateExplorerState first;
  protected StateExplorerState second;
  protected StateExplorerState third;
  
  @Before
  public void setUp() {
    Collection<Action> actions1 = Lists.newArrayList();
    actions1.add(new ClickAction(new IdWebElementIdentifier("id1")));
    actions1.add(new ClickAction(new IdWebElementIdentifier("id2")));
    
    Collection<State> states1 = Lists.newArrayList();
    states1.add(new CountOfElementsState(1));
    states1.add(new CountOfElementsState(2));

    first = new StateExplorerState(states1, actions1);

    Collection<Action> actions2 = Lists.newArrayList();
    actions2.add(new ClickAction(new IdWebElementIdentifier("id2")));
    actions2.add(new ClickAction(new IdWebElementIdentifier("id1")));
    
    Collection<State> states2 = Lists.newArrayList();
    states2.add(new CountOfElementsState(2));
    states2.add(new CountOfElementsState(1));

    second = new StateExplorerState(states2, actions2);

    Collection<Action> actions3 = Lists.newArrayList();
    actions3.add(new ClickAction(new IdWebElementIdentifier("id1")));
    actions3.add(new ClickAction(new IdWebElementIdentifier("id3")));
    
    Collection<State> states3 = Lists.newArrayList();
    states3.add(new CountOfElementsState(1));
    states3.add(new CountOfElementsState(3));
    
    third = new StateExplorerState(states3, actions3);
  }  
}
