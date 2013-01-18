/*
Copyright 2011 Google Inc. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package org.webtestingexplorer.identifiers;

import com.google.common.collect.Lists;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.webtestingexplorer.driver.WebDriverWrapper;
import org.webtestingexplorer.driver.WebElementWrapper;

import java.util.List;

/**
 * Identifies an element by its index in the list that we build of all
 * elements in the browser. This will be the least reliable identifier that
 * we use when the other approaches cannot be used.
 * 
 * @author smcmaster@google.com (Scott McMaster)
 */
public class IndexWebElementIdentifier extends WebElementIdentifier {

  /**
   * Whether this index is relative to the actionable or stateful elements.
   */
  public enum IndexBasis {
    ACTIONABLE,
    STATEFUL
  }
  
  protected int index;
  private IndexBasis basis;

  protected IndexWebElementIdentifier() {
    // For xstream.
    super();
  }
  
  public IndexWebElementIdentifier(int index) {
    this(index, null, IndexBasis.STATEFUL);
  }
  
  public IndexWebElementIdentifier(int index, String frameIdentifier, IndexBasis basis) {
    super(frameIdentifier);
    this.index = index;
    this.basis = basis;
  }

  public int getIndex() {
    return index;
  }
  
  /**
   * Finds all the web elements with the current basis using the
   * appropriate selector.
   */
  protected List<WebElementWithIdentifier> getAllElementsWithBasis(WebDriverWrapper driver) {
    List<WebElementWithIdentifier> allElements;
    if (basis == IndexBasis.ACTIONABLE) {
      allElements = driver.getActionableElementsForFrame(frameIdentifier);
    } else {
      allElements = driver.getStatefulElementsForFrame(frameIdentifier);
    }
    assert index < allElements.size();
    return allElements;
  }

  @Override
  public WebElementWrapper findElement(WebDriverWrapper driver) {
    return new WebElementWrapper(getAllElementsWithBasis(driver).get(index).getElement());
  }

  @Override
  public List<WebElementWrapper> findElements(WebDriverWrapper driver) {
    return Lists.newArrayList(findElement(driver));
  }

  @Override
  public String toString() {
    return super.toString() + ",index=" + index + ",basis=" + basis;
  }
  
  @Override
  public boolean equals(Object obj) {
  	if (obj == this) {
  		return true;
  	}
  	if (!(obj instanceof IndexWebElementIdentifier)) {
  		return false;
  	}
  	IndexWebElementIdentifier other = (IndexWebElementIdentifier) obj;
    return new EqualsBuilder().appendSuper(super.equals(obj))
        .append(index, other.index)
        .append(basis, other.basis)
        .isEquals();
  }
  
  @Override
  public int hashCode() {
    return new HashCodeBuilder().appendSuper(super.hashCode())
        .append(index)
        .append(basis)
        .hashCode();
  }
}
