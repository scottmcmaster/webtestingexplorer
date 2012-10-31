package org.webtestingexplorer.identifiers;

import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.webtestingexplorer.driver.WebDriverWrapper;
import org.webtestingexplorer.driver.WebElementWrapper;

/**
 * Identifies an element by index among the set of elements with the
 * specific tag.
 * 
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
public class TagIndexWebElementIdentifier extends IndexWebElementIdentifier {

	private String tagName;
	
	public TagIndexWebElementIdentifier(String frameIdentifier,
			String tagName, int index, IndexBasis basis) {
		super(index, frameIdentifier, basis);
		this.tagName = tagName;
	}

	public String getTagName() {
	  return tagName;
	}
	
	@Override
	public WebElementWrapper findElement(WebDriverWrapper driver) {
		List<WebElementWithIdentifier> allElements = getAllElementsWithBasis(driver);
		int currentIndex = 0;
		for (WebElementWithIdentifier element : allElements) {
			if (tagName.equals(element.getElement().getTagName()) &&
					currentIndex == index) {
				return new WebElementWrapper(element.getElement());
			}
			++currentIndex;
		}
		return null;
	}

  @Override
  public String toString() {
    return super.toString() + ",tag=" + tagName;
  }
  
  @Override
  public boolean equals(Object obj) {
  	if (obj == this) {
  		return true;
  	}
  	if (!(obj instanceof TagIndexWebElementIdentifier)) {
  		return false;
  	}
  	TagIndexWebElementIdentifier other = (TagIndexWebElementIdentifier) obj;
    return new EqualsBuilder().appendSuper(super.equals(obj))
        .append(tagName, other.tagName)
        .isEquals();
  }
  
  @Override
  public int hashCode() {
    return new HashCodeBuilder().appendSuper(super.hashCode())
    		.append(tagName).hashCode();
  }
}
