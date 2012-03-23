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
package com.google.testing.webtestingexplorer.state;

import com.google.common.collect.Lists;
import com.google.testing.webtestingexplorer.driver.WebDriverWrapper;
import com.google.testing.webtestingexplorer.identifiers.WebElementIdentifier;
import com.google.testing.webtestingexplorer.identifiers.WebElementWithIdentifier;

import org.openqa.selenium.WebElement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * State only contains information on currently visible elements.
 * 
 * @author xyuan@google.com (Xun Yuan)
 */
public class VisibleElementsState extends ElementsState {
  
  public VisibleElementsState(WebDriverWrapper driver) {
    List<WebElementWithIdentifier> statefulElements = driver.getStatefulElements();
  	elementType = ElementType.VISIBLE;
  	
  	List<WebElementWithIdentifier> visibleElements = Lists.newArrayList();
    for (WebElementWithIdentifier e: statefulElements) {
      if (e.getElement().isDisplayed()) {
        visibleElements.add(e);
      }
    }
	  
  	elementProperties = collectProperties(driver, visibleElements);
  }
  
  /**
   * Get information of a WebElement.
   */
  private Map<WebElementIdentifier, Map<String, String>> collectProperties(
      WebDriverWrapper driver,
      List<WebElementWithIdentifier> elements) {
    Map<WebElementIdentifier, Map<String, String>> allProperties =
        new HashMap<WebElementIdentifier, Map<String, String>>();

    for (WebElementWithIdentifier elementWithId : elements) {
      // Get all properties
      WebElement e = elementWithId.getElement();
      Map<String, String> properties = new HashMap<String, String>();
      String value = e.getTagName();
      if (value != null) {
        properties.put(TAG_NAME, value);
      }

      value = e.getText();
      if (value != null) {
        properties.put(VALUE, value);
      }

      properties.put(ENABLED, Boolean.toString(e.isEnabled()));
      properties.put(SELECTED, Boolean.toString(e.isSelected()));

      for (String attribute : ELEMENT_ATTRIBUTES) {
        String attributeValue = e.getAttribute(attribute);
        if (attributeValue != null) {
          properties.put(attribute, attributeValue);
        }
      }

      allProperties.put(elementWithId.getIdentifier(), properties);
    }

    return allProperties;
  }

  @Override
  public StateChecker createStateChecker() {
    return new VisibleElementsStateChecker();
  }
}
