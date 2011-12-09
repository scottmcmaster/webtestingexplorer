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

import java.util.Collection;
import java.util.Map;

import org.openqa.selenium.WebElement;

import com.google.testing.webtestingexplorer.driver.WebDriverWrapper;

/**
 * State only contains information on currently visible elements.
 * 
 * @author xyuan@google.com (Xun Yuan)
 */
public class VisibleElementsState extends ElementsState {
  
  public VisibleElementsState(WebDriverWrapper driver) {
    Map<Integer, WebElement> elements = driver.getVisibleElements();
  	eventType = ElementType.VISIBLE;
	  
  	if (areElementsValid(elements.values())) {
  	  elementProperties = collectProperties(driver, elements);
  	} else {
  	  elementProperties = null;
  	}
  }
  
  /**
   * Check whether elements are visible.
   */
  @Override
  protected boolean areElementsValid(Collection<WebElement> elements) {
		boolean valid = true;
	
	  for (WebElement e: elements) {
	    if (!e.isDisplayed()) {
	    	valid = false;
	    	break;
	    }
	  }
	  return valid;
	}
}
