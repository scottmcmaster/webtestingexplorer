package org.webtestingexplorer.testcase;

import org.webtestingexplorer.actions.Action;
import org.webtestingexplorer.actions.BackAction;
import org.webtestingexplorer.actions.ClickAction;
import org.webtestingexplorer.actions.ForwardAction;
import org.webtestingexplorer.actions.HoverAction;
import org.webtestingexplorer.actions.RefreshAction;
import org.webtestingexplorer.actions.SelectAction;
import org.webtestingexplorer.actions.SetTextAction;
import org.webtestingexplorer.driver.ActionSequenceRunner.ActionSequenceResult;
import org.webtestingexplorer.identifiers.ClassIndexWebElementIdentifier;
import org.webtestingexplorer.identifiers.IdWebElementIdentifier;
import org.webtestingexplorer.identifiers.IndexWebElementIdentifier;
import org.webtestingexplorer.identifiers.NameWebElementIdentifier;
import org.webtestingexplorer.identifiers.TagIndexWebElementIdentifier;
import org.webtestingexplorer.identifiers.WebElementIdentifier;
import org.webtestingexplorer.identifiers.XpathWebElementIdentifier;
import org.webtestingexplorer.oracles.FailureReason;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Outputs test cases in a nice readable format better suited for manual re-execution.
 * 
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
public class PrettyTestCaseWriter extends AbstractTestCaseWriter {

  private final static Logger LOGGER =
      Logger.getLogger(PrettyTestCaseWriter.class.getName());

  private String outputDirectory;
  
  public PrettyTestCaseWriter(String outputDirectory, boolean writeOnFailureOnly) {
    this.outputDirectory = outputDirectory;
    this.writeOnFailureOnly = writeOnFailureOnly;
  }

  public PrettyTestCaseWriter(String outputDirectory) {
    this(outputDirectory, false);
  }

  /**
   * Writes a test case based on the given action sequence.
   */
  @Override
  public void writeTestCase(TestCase testCase, int testCaseNumber, ActionSequenceResult result) {
    String fileName = "test-" + testCaseNumber + ".txt";
    String fullPath = outputDirectory + "/" + fileName;
    LOGGER.log(Level.INFO, "Writing test case to " + fullPath);
    BufferedWriter out = null;
    try {
      out = new BufferedWriter(new FileWriter(new File(fullPath)));
      int stepNum = 1;
      for (Action action : testCase.getActionSequence().getActions()) {
        String line = "" + stepNum + ". " + formatAction(action);
        out.write(line);
        out.newLine();
        ++stepNum;
      }
      out.newLine();
      
      if (!result.hasFailures()) {
        out.write("PASSED");
      } else {
        out.write("FAILED");
        for (FailureReason reason : result.getFailures()) {
          out.write(reason.getMessage());
          out.newLine();
        }
      }
      out.newLine();
    } catch (Exception e) {
      System.err.println("Failed to write " + fullPath);
      e.printStackTrace();
    }
    finally {
      try { out.close(); } catch (Exception e) {}
    }
  }

  /**
   * Formats the given action in the "pretty" format.
   */
  private String formatAction(Action action) {
    if (action instanceof BackAction) {
      return "Click the Back button";
    } else if (action instanceof ForwardAction) {
      return "Click the Forward button";
    } else if (action instanceof RefreshAction) {
      return "Refresh the page";
    } else if (action instanceof ClickAction) {
      return "Click " + formatIdentifier(action.getIdentifier());
    } else if (action instanceof HoverAction) {
      return "Hover over " + formatIdentifier(action.getIdentifier());
    } else if (action instanceof SetTextAction) {
      return "Set the text in " + formatIdentifier(action.getIdentifier()) + " to '" +
          ((SetTextAction) action).getKeysToSend() + "'";
    } else if (action instanceof SelectAction) {
      return "Select option " + ((SelectAction) action).getOptionIndex() + " from " +
          formatIdentifier(action.getIdentifier());
    }
    return action.toString();
  }

  /**
   * Formats a web element identifer in the "pretty" format.
   */
  private String formatIdentifier(WebElementIdentifier identifier) {
    String result = null;
    if (identifier instanceof IdWebElementIdentifier) {
      return "the element with id " + ((IdWebElementIdentifier) identifier).getId();
    } else if (identifier instanceof NameWebElementIdentifier) {
      return "the element named " + ((NameWebElementIdentifier) identifier).getName();
    } else if (identifier instanceof XpathWebElementIdentifier) {
      return "the element at xpath " + ((XpathWebElementIdentifier) identifier).getXpath();
    } else if (identifier instanceof ClassIndexWebElementIdentifier) {
      result = "the element with class " + ((ClassIndexWebElementIdentifier) identifier).getClassName();
    } else if (identifier instanceof TagIndexWebElementIdentifier) {
      result = "the element with tag " + ((TagIndexWebElementIdentifier) identifier).getTagName();
    } else if (identifier instanceof IndexWebElementIdentifier) {
      result = "the element at index " + ((IndexWebElementIdentifier) identifier).getIndex();
    }
    
    if (result != null) {
    	if (identifier instanceof IndexWebElementIdentifier) {
    		result += " at index " + ((IndexWebElementIdentifier) identifier).getIndex();
    	}
      if (identifier.getFrameIdentifier() != null) {
        result += " in frame " + identifier.getFrameIdentifier();
      }
      return result;
    }
    
    return identifier.toString();
  }
}
