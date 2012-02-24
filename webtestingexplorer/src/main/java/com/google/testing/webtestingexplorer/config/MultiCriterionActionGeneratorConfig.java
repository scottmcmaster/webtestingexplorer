/*
Copyright 2012 Google Inc. All Rights Reserved.

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

package com.google.testing.webtestingexplorer.config;

import com.google.testing.webtestingexplorer.identifiers.WebElementWithIdentifier;

import java.util.regex.Pattern;

/**
 * Action generator config that can look at multiple properties of an element
 * (id, name, tag, others?). The attributes are matched against Java-syntax
 * regular expressions.
 * 
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
public abstract class MultiCriterionActionGeneratorConfig implements ActionGeneratorConfig {

  private final Pattern tagPattern;
  private final Pattern idPattern;
  private final Pattern namePattern;
  private final Pattern classNamePattern;
  
  /**
   * Any of the criteria that you don't want to check may be left null.
   * If all are null, the generator config does not match anything.
   */
  public MultiCriterionActionGeneratorConfig(String tag, String id,
      String name, String className) {
    if (tag != null) {
      this.tagPattern = Pattern.compile(tag);
    } else {
      this.tagPattern = null;
    }
    if (id != null) {
      this.idPattern = Pattern.compile(id);
    } else {
      this.idPattern = null;
    }
    if (name != null) {
      this.namePattern = Pattern.compile(name);
    } else {
      this.namePattern = null;
    }
    if (className != null) {
      this.classNamePattern = Pattern.compile(className);
    } else {
      this.classNamePattern = null;
    }
  }

  @Override
  public boolean matches(WebElementWithIdentifier elementWithId) {
    
    boolean isMatch = true;
    if (tagPattern != null) {
      String tag = elementWithId.getElement().getTagName();
      if (tag != null && tag.length() > 0) {
        isMatch = isMatch && tagPattern.matcher(tag).matches();
      } else {
        return false;
      }
    }
    if (idPattern != null) {
      String id = elementWithId.getElement().getAttribute("id");
      if (id != null && id.length() > 0) {
        isMatch = isMatch && idPattern.matcher(id).matches();
      } else {
        return false;
      }
    }
    if (namePattern != null) {
      String name = elementWithId.getElement().getAttribute("name");
      if (name != null && name.length() > 0) {
        isMatch = isMatch && namePattern.matcher(name).matches();
      } else {
        return false;
      }
    }
    if (classNamePattern != null) {
      String className = elementWithId.getElement().getAttribute("class");
      if (className != null && className.length() > 0) {
        isMatch = isMatch && classNamePattern.matcher(className).matches();
      } else {
        return false;
      }
    }
    return isMatch;
  }
}
