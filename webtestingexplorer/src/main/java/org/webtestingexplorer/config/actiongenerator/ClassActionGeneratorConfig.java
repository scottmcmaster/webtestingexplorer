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
package org.webtestingexplorer.config.actiongenerator;

import com.google.common.collect.Sets;

import org.webtestingexplorer.config.ActionGeneratorConfig;
import org.webtestingexplorer.identifiers.WebElementWithIdentifier;

import java.util.Set;

/**
 * Matches elements based on the value of the class attribute.
 * 
 * @author smcmaster@google.com (Scott McMaster)
 */
public abstract class ClassActionGeneratorConfig implements ActionGeneratorConfig {

  private String classValue;
  
  public ClassActionGeneratorConfig(String classValue) {
    this.classValue = classValue;
  }
  
  @Override
  public boolean matches(WebElementWithIdentifier elementWithId) {
    String classAttributeValue = elementWithId.getElement().getAttribute("class");
    Set<String> classes = Sets.newHashSet(classAttributeValue.split("\\w"));
    return classes.contains(classValue);
  }
}