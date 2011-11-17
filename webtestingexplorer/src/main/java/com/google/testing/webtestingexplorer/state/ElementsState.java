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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.WebElement;

import com.google.testing.webtestingexplorer.element.WebElementUtil;
import com.google.testing.webtestingexplorer.identifiers.WebElementIdentifier;

/**
 * Parent class of element related state.
 * 
 * @author xyuan@google.com (Xun Yuan)
 */
public abstract class ElementsState implements State {
	Map<WebElementIdentifier, List<String>> elementProperties;
	
	WebElementUtil.ElementType eventType;
	
	abstract boolean areElementsValid(Collection<WebElement> elements);
	
	/**
	 * Get information of a WebElement.
	 */
	protected Map<WebElementIdentifier, List<String>> collectProperties(
			Map<Integer, WebElement> elements) {
		Map<WebElementIdentifier, List<String>> allProperties = 
				new HashMap<WebElementIdentifier, List<String>>();
		
		for (Map.Entry<Integer, WebElement> element: elements.entrySet()) {
			// Get all properties
			WebElement e = element.getValue();
			List<String> properties = new ArrayList<String>();
			String value = e.getTagName();
			properties.add(value == null? "" : value);
			
			value = e.getText();
			properties.add(value == null? "" : value);
			
			properties.add(Boolean.toString(e.isEnabled()));
			properties.add(Boolean.toString(e.isSelected()));
			
			for (String attribute: WebElementUtil.elementAttributes) {
				String attributeValue = e.getAttribute(attribute);
				properties.add(attributeValue == null? "" : attributeValue);
			}
			
			// Generate identifier
			WebElementIdentifier identifier = 
					WebElementUtil.generateIdentifier(element.getKey().intValue(), e);
			
			allProperties.put(identifier, properties);
		}
		
		return allProperties;
	}
	
	/**
	 * Compare whether two states are equal.
	 */
	@Override
	public boolean equals(Object other) {
		if (other == this) {
		  return true;
		}
		if (!(other instanceof ElementsState)) {
		  return false;
		}
	
		ElementsState otherState = (ElementsState) other;
		int propertiesSize = elementProperties.size();
		if (propertiesSize != otherState.elementProperties.size()) {
			return false;
		}
		
		boolean isEqual = true;
		for (Map.Entry<WebElementIdentifier, List<String>> entry : elementProperties.entrySet())
		{
			List<String> otherProperties = otherState.elementProperties.get(entry.getKey());
			if (otherProperties == null) {
				isEqual = false;
				break;
			}
			
			List<String> theseProperties = entry.getValue();

			for (int j = 0; j < theseProperties.size(); ++j) {
				if (!theseProperties.get(j).equalsIgnoreCase(otherProperties.get(j))) {
					isEqual = false;
					break;
				}
			}		
			if (!isEqual) {
				break;
			}
		}

		return isEqual;
	}	
} 