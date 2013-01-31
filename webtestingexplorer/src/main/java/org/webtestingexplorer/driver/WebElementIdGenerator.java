package org.webtestingexplorer.driver;

import org.openqa.selenium.WebElement;
import org.webtestingexplorer.identifiers.ClassIndexWebElementIdentifier;
import org.webtestingexplorer.identifiers.IdWebElementIdentifier;
import org.webtestingexplorer.identifiers.IndexWebElementIdentifier;
import org.webtestingexplorer.identifiers.IndexWebElementIdentifier.IndexBasis;
import org.webtestingexplorer.identifiers.NameWebElementIdentifier;
import org.webtestingexplorer.identifiers.TagIndexWebElementIdentifier;
import org.webtestingexplorer.identifiers.WebElementIdentifier;

/**
 * Generates {@link WebElementIdentifier}s for specific {@link WebElement}s.
 * This class contains methods that depend on webdriver internal id's...hmmm...
 * 
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
public class WebElementIdGenerator {

	/**
   * Generate identifier for a WebElement.
   */
  public WebElementIdentifier generateIdentifier(int elementIndex, WebElement element,
      String frameIdentifier, IndexBasis elementIndexBasis) {
    String id = element.getAttribute("id");
    WebElementIdentifier identifier = null;
    
    // id is first priority.
    if (id != null && id.length() > 0) {
      identifier = new IdWebElementIdentifier(id, frameIdentifier);
    }
    
    if (identifier == null) {
      // name is second.
      String name = element.getAttribute("name");
      if (name != null && name.length() > 0) {
        identifier = new NameWebElementIdentifier(name, frameIdentifier);
      }
    }
    
    if (identifier == null) {
      // For form elements, we will find their index amongst the same tags.
      if (isFormElement(element)) {
        identifier = new TagIndexWebElementIdentifier(frameIdentifier, element.getTagName(),
      			elementIndex, elementIndexBasis);
      }
    }
    
    if (identifier == null) {
      // If it has a class, use the index into elements with the same class.
      String className = element.getAttribute("class");
      if (className != null && !className.isEmpty()) {
        identifier = new ClassIndexWebElementIdentifier(frameIdentifier, className, elementIndex, elementIndexBasis);
      }
    }
    
    if (identifier == null) {
      // Fall back on index in the selector set. This is by far the least
      // stable.
      // TODO(smcmaster): We could perhaps make this more stable by always
      // using TagIndexWebElementIdentifier, but I'm worried about how that
      // will affect runtime performance.
      identifier = new IndexWebElementIdentifier(elementIndex, frameIdentifier, elementIndexBasis);
    }
    
    // Only add the innerHTML to actionable elements (for debugging).
    if (elementIndexBasis == IndexBasis.ACTIONABLE) {
      identifier.setOuterHtml(element.getAttribute("outerHTML"));
    }
    return identifier;
  }
  
  /**
   * For form elements, we will create tag+index indentifiers
   * because we expect there won't be too many and they will be relatively
   * static.
   */
  private boolean isFormElement(WebElement element) {
    String tagName = element.getTagName();
    if ("input".equals(tagName) ||
    		"button".equals(tagName) ||
    		"textarea".equals(tagName) ||
    		"select".equals(tagName)) {
    	return true;
    }
    return false;
  }

}
