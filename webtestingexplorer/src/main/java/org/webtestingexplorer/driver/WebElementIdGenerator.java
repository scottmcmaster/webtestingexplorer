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
    
    // id is first priority.
    if (id != null && id.length() > 0) {
      return new IdWebElementIdentifier(id, frameIdentifier);
    }
    
    // name is second.
    String name = element.getAttribute("name");
    if (name != null && name.length() > 0) {
      return new NameWebElementIdentifier(name, frameIdentifier);
    }
    
    // For form elements, we will find their index amongst the same tags.
    if (isFormElement(element)) {
    	return new TagIndexWebElementIdentifier(frameIdentifier, element.getTagName(),
    			elementIndex, elementIndexBasis);
    }
    
    // If it has a class, use the index into elements with the same class.
    String className = element.getAttribute("class");
    if (className != null && !className.isEmpty()) {
    	return new ClassIndexWebElementIdentifier(frameIdentifier, className, elementIndex, elementIndexBasis);
    }
    
    // Fall back on index in the selector set. This is by far the least
    // stable.
    // TODO(smcmaster): We could perhaps make this more stable by always
    // using TagIndexWebElementIdentifier, but I'm worried about how that
    // will affect runtime performance.
    return new IndexWebElementIdentifier(elementIndex, frameIdentifier, elementIndexBasis);          
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
