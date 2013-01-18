package org.webtestingexplorer.identifiers;

import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.webtestingexplorer.driver.WebDriverWrapper;
import org.webtestingexplorer.driver.WebElementWrapper;

/**
 * Identifies an element by index among the set of elements with the specific CSS class name. The
 * class name must be an exact match.
 *
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
public class ClassIndexWebElementIdentifier extends IndexWebElementIdentifier {

  private final static Logger LOGGER =
      Logger.getLogger(ClassIndexWebElementIdentifier.class.getName());

  private String className;

  protected ClassIndexWebElementIdentifier() {
    // For xstream.
    super();
  }
  
  public ClassIndexWebElementIdentifier(
      String frameIdentifier, String className, int index, IndexBasis basis) {
    super(index, frameIdentifier, basis);
    this.className = className;
  }

  public String getClassName() {
    return className;
  }

  @Override
  public WebElementWrapper findElement(WebDriverWrapper driver) {
    List<WebElementWithIdentifier> allElements = getAllElementsWithBasis(driver);
    int currentIndex = 0;
    for (WebElementWithIdentifier element : allElements) {
      if (className.equals(element.getElement().getAttribute("class")) && currentIndex == index) {
        return new WebElementWrapper(element.getElement());
      }
      ++currentIndex;
    }
    LOGGER.warning("Failed to find " + toString());
    return null;
  }

  @Override
  public String toString() {
    return super.toString() + ",class=" + className;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof ClassIndexWebElementIdentifier)) {
      return false;
    }
    ClassIndexWebElementIdentifier other = (ClassIndexWebElementIdentifier) obj;
    return new EqualsBuilder().appendSuper(super.equals(obj))
        .append(className, other.className).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().appendSuper(super.hashCode()).append(className).hashCode();
  }
}
