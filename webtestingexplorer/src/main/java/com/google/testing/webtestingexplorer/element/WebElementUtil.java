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
package com.google.testing.webtestingexplorer.element;

import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.WebElement;

import com.google.testing.webtestingexplorer.identifiers.IdWebElementIdentifier;
import com.google.testing.webtestingexplorer.identifiers.IndexWebElementIdentifier;
import com.google.testing.webtestingexplorer.identifiers.NameWebElementIdentifier;
import com.google.testing.webtestingexplorer.identifiers.WebElementIdentifier;

/**
 * Utility class for WebElement.
 * 
 * @author xyuan@google.com (Xun Yuan)
 */
public class WebElementUtil {
	
  //Add more when needed
	static public List<String> elementAttributes = Arrays.asList(
			"id", "class", "type", "title", "role", "style");
	
	public enum ElementType {
   ALL, VISIBLE, ACTIONABLE
  }
	
	/*
	 * Generate identifier for a WebElement.
	 */
	static public WebElementIdentifier generateIdentifier(int elementIndex, WebElement element) {
		String name = element.getAttribute("name");
    String id = element.getAttribute("id");
    
    WebElementIdentifier identifier;
    if (id != null && id.length() > 0) {
      identifier = new IdWebElementIdentifier(id);
    } else if (name != null && name.length() > 0) {
      identifier = new NameWebElementIdentifier(name);
    } else {
      identifier = new IndexWebElementIdentifier(elementIndex);
    }
    
    return identifier;
	}

}
