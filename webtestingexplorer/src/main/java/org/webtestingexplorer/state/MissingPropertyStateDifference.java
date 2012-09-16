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
package org.webtestingexplorer.state;

/**
 * A difference in a property which has two different values.
 * 
 * @author xyuan@google.com (Xun Yuan)
 */
public class MissingPropertyStateDifference implements StateDifference {

  private final String property;
  private final Object firstStateValue;
  private final Object secondStateValue;
  
  public MissingPropertyStateDifference(String property, Object firstStateValue, Object secondStateValue) {
    this.property = property;
    this.firstStateValue = firstStateValue;
    this.secondStateValue = secondStateValue;
  }

  public String getProperty() {
    return property;
  }
  
  @Override
  public String toString() {
    return formatDifference();
  }

  @Override
  public String formatFirstValue() {
    return (firstStateValue != null ? firstStateValue.toString() : "null");
  }

  @Override
  public String formatSecondValue() {
    return (secondStateValue != null ? secondStateValue.toString() : "null");
  }

  @Override
  public String formatDifference() {
    return "DiffKey:prop-" + property + 
        " V1:" + formatFirstValue() + " V2:" + formatSecondValue();
  }
}
