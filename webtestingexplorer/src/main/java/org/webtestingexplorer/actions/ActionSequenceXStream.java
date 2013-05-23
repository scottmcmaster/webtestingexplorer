package org.webtestingexplorer.actions;


import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

import org.webtestingexplorer.actions.Action;
import org.webtestingexplorer.actions.ActionSequence;
import org.webtestingexplorer.actions.BackAction;
import org.webtestingexplorer.actions.ClickAction;
import org.webtestingexplorer.actions.CompositeAction;
import org.webtestingexplorer.actions.ForwardAction;
import org.webtestingexplorer.actions.HoverAction;
import org.webtestingexplorer.actions.RefreshAction;
import org.webtestingexplorer.actions.SelectAction;
import org.webtestingexplorer.actions.SetTextAction;
import org.webtestingexplorer.actions.WaitAction;
import org.webtestingexplorer.identifiers.AttributeValueWebElementIdentifier;
import org.webtestingexplorer.identifiers.ClassIndexWebElementIdentifier;
import org.webtestingexplorer.identifiers.IdWebElementIdentifier;
import org.webtestingexplorer.identifiers.IndexWebElementIdentifier;
import org.webtestingexplorer.identifiers.NameWebElementIdentifier;
import org.webtestingexplorer.identifiers.TagIndexWebElementIdentifier;
import org.webtestingexplorer.identifiers.WebElementIdentifier;
import org.webtestingexplorer.identifiers.XpathWebElementIdentifier;
import org.webtestingexplorer.identifiers.IndexWebElementIdentifier.IndexBasis;

/**
 * XStream configuration for reading and writing action sequences.
 * 
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
public class ActionSequenceXStream {

  /**
   * Creates a correctly-configured {@link XStream} instance.
   */
  public static XStream createXStream() {
    XStream xstream = new XStream(new StaxDriver());
    addActionSequenceAliases(xstream);
    
    // TODO(smcmaster): I think we need to expose some API here so that users can
    // register their custom classes to be serialized nicely in the test case format.
    return xstream;
  }

  /**
   * Adds the xstream aliases necessary to write nice {@link ActionSequence}s.
   */
  public static void addActionSequenceAliases(XStream xstream) {
    xstream.alias("ActionSequence", ActionSequence.class);
    xstream.alias("ActionSequenceQueue", ActionSequenceQueue.class);
    xstream.alias("Action", Action.class);
    xstream.alias("BackAction", BackAction.class);
    xstream.alias("ClickAction", ClickAction.class);
    xstream.alias("ForwardAction", ForwardAction.class);
    xstream.alias("RefreshAction", RefreshAction.class);
    xstream.alias("SelectAction", SelectAction.class);
    xstream.alias("SetTextAction", SetTextAction.class);
    xstream.alias("HoverAction", HoverAction.class);
    xstream.alias("WaitAction", WaitAction.class);
    xstream.alias("CompositeAction", CompositeAction.class);
    xstream.alias("WebElementIdentifier", WebElementIdentifier.class);
    xstream.alias("NameWebElementIdentifier", NameWebElementIdentifier.class);
    xstream.alias("IdWebElementIdentifier", IdWebElementIdentifier.class);
    xstream.alias("IndexWebElementIdentifier", IndexWebElementIdentifier.class);
    xstream.alias("XpathWebElementIdentifier", XpathWebElementIdentifier.class);
    xstream.alias("AttributeValueWebElementIdentifier", AttributeValueWebElementIdentifier.class);
    xstream.alias("TagIndexWebElementIdentifier", TagIndexWebElementIdentifier.class);
    xstream.alias("ClassIndexWebElementIdentifier", ClassIndexWebElementIdentifier.class);
    xstream.alias("IndexBasis", IndexBasis.class);
    xstream.addImmutableType(IndexBasis.class);
  }
}
