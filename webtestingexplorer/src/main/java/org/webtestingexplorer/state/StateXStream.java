package org.webtestingexplorer.state;


import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

/**
 * XStream configuration for reading and writing state data.
 * 
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
public class StateXStream {

  /**
   * Creates a correctly-configured {@link XStream} instance.
   */
  public static XStream createXStream() {
    XStream xstream = new XStream(new StaxDriver());
    addStateAliases(xstream);
    
    // TODO(smcmaster): I think we need to expose some API here so that users can
    // register their custom classes to be serialized nicely in the test case format.
    return xstream;
  }
  
  /**
   * Adds the xstream aliases necessary to write {@link State} classes.
   */
  public static void addStateAliases(XStream xstream) {
    xstream.alias("CountOfElementsState", CountOfElementsState.class);
    xstream.alias("CustomizedPropertiesElementsState", CustomizedPropertiesElementsState.class);
    xstream.alias("ElementsState", ElementsState.class);
    xstream.alias("VisibleElementsState", VisibleElementsState.class);
    xstream.alias("SelectedElementsState", SelectedElementsState.class);
  }

}
