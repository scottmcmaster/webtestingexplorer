package org.webtestingexplorer.actions;

import com.thoughtworks.xstream.XStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Contains the set of {@link ActionSequence}s that we want to run.
 * 
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
public class ActionSequenceQueue implements Iterable<ActionSequence> {
  
  private final static Logger LOGGER =
      Logger.getLogger(ActionSequenceQueue.class.getName());

  private static XStream xstream = ActionSequenceXStream.createXStream();

  /**
   * Writes the given action sequence queue to the given filesystem location,
   * overwriting if it already exists.
   */
  public static void writeToFile(ActionSequenceQueue queue, String filename) {
    LOGGER.info("Writing action sequence queue to file: " + filename);
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
   * Reads an action sequence queue from the given file system location.
   * 
   * @return the read-in action sequence queue, or null if the file does not exist.
   */
  public static ActionSequenceQueue readFromFile(String filename) {
    LOGGER.info("Reading action sequence queue from file: " + filename);
    File inputFile = new File(filename);
    FileInputStream inputStream = null;
    try {
      inputStream = new FileInputStream(inputFile);
      return (ActionSequenceQueue) xstream.fromXML(inputStream);
    } catch (FileNotFoundException e) {
      return null;
    } finally {
      if (inputStream != null) {
        try { inputStream.close(); } catch (Exception e) {}      }
    }
  }

  private Deque<ActionSequence> actionSequences;

  public ActionSequenceQueue() {
    actionSequences = new ArrayDeque<ActionSequence>();
  }
  
  public ActionSequenceQueue(Iterable<ActionSequence> allSequences) {
    this();
    for (ActionSequence sequence : allSequences) {
      actionSequences.push(sequence);
    }
  }

  public boolean isEmpty() {
    return actionSequences.isEmpty();
  }

  public int size() {
    return actionSequences.size();
  }

  public ActionSequence pop() {
    return actionSequences.pop();
  }

  public void push(ActionSequence sequence) {
    actionSequences.push(sequence);
  }

  @Override
  public Iterator<ActionSequence> iterator() {
    return actionSequences.iterator();
  }  
}
