package org.webtestingexplorer.stateexplorer;


import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

import org.webtestingexplorer.actions.ActionSequence;
import org.webtestingexplorer.actions.ActionSequenceXStream;
import org.webtestingexplorer.state.StateXStream;

/**
 * XStream configuration for reading and writing state explorer data.
 * 
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
public class StateExplorerXStream {

  /**
   * Creates a correctly-configured {@link XStream} instance.
   */
  public static XStream createXStream() {
    XStream xstream = new XStream(new StaxDriver());
    ActionSequenceXStream.addActionSequenceAliases(xstream);
    StateXStream.addStateAliases(xstream);
    addStateExplorerAliases(xstream);
    return xstream;
  }

  /**
   * Adds the xstream aliases necessary to write nice {@link ActionSequence}s.
   */
  public static void addStateExplorerAliases(XStream xstream) {
    xstream.alias("StateExplorerState", StateExplorerState.class);
    xstream.alias("StateExplorerStateGraph", StateExplorerStateGraph.class);
  }
}
