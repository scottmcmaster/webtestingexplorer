package org.webtestingexplorer.stateexplorer;

import com.google.common.collect.Sets;

import com.thoughtworks.xstream.XStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Graph of the explored {@link StateExplorerState}s.
 */
public class StateExplorerStateGraph {

  private final static Logger LOGGER =
      Logger.getLogger(StateExplorerStateGraph.class.getName());

  private static XStream xstream = StateExplorerXStream.createXStream();

  private StateExplorerState rootState;
  
  public StateExplorerState getRootState() {
    return rootState;
  }
  
  public void setRootState(StateExplorerState newState) {
    this.rootState = newState;
  }
  
  /**
   * Looks for the given state in the graph, returning the one that is already
   * there, or the given state if it's not found. So basically, the return value
   * of this method should either be added to the graph at the appropriate location,
   * or already in the graph and just needs to be hooked up.
   */
  public StateExplorerState findState(StateExplorerState newState) {
    if (rootState == null) {
      rootState = newState;
      return rootState;
    }
    Set<StateExplorerState> markedStates = Sets.newHashSet();
    StateExplorerState foundState = findStateHelper(rootState, newState, markedStates);
    if (foundState == null) {
      return newState;
    }
    return foundState;
  }

  /**
   * Recursive helper for findState().
   * @param markedStates 
   */
  private StateExplorerState findStateHelper(StateExplorerState currentState, StateExplorerState newState,
      Set<StateExplorerState> markedStates) {
    markedStates.add(currentState);
    if (currentState.equals(newState)) {
      return currentState;
    }
    for (StateExplorerState childState : currentState.getFollowingStates()) {
      StateExplorerState findState = findStateHelper(childState, newState, markedStates);
      if (findState != null) {
        return findState;
      }
    }
    return null;
  }
  
  /**
   * Writes the given state graph to the given filesystem location,
   * overwriting if it already exists.
   */
  public static void writeToFile(StateExplorerStateGraph queue, String filename) {
    LOGGER.info("Writing state graph to file: " + filename);
    String xml = xstream.toXML(queue);
    Writer out = null;
    try {
      out = new OutputStreamWriter(new FileOutputStream(filename));
      out.write(xml);
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Failed to write " + filename, e);
    }
    finally {
      try { out.close(); } catch (Exception e) {}
    }
  }

  /**
   * Reads a state graph from the given file system location.
   * 
   * @return the read-in state graph, or null if the file does not exist.
   */
  public static StateExplorerStateGraph readFromFile(String filename) {
    LOGGER.info("Reading action sequence queue from file: " + filename);
    File inputFile = new File(filename);
    FileInputStream inputStream = null;
    try {
      inputStream = new FileInputStream(inputFile);
      return (StateExplorerStateGraph) xstream.fromXML(inputStream);
    } catch (FileNotFoundException e) {
      return null;
    } finally {
      if (inputStream != null) {
        try { inputStream.close(); } catch (Exception e) {}      }
    }
  }
}
