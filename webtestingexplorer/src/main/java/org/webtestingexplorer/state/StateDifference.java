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
 * Represents a difference between two states.
 * 
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
public interface StateDifference {

  /**
   * Formats the first state's "value" as a string.
   */
  String formatFirstValue();
  
  /**
   * Formats the second state's "value" as a string.
   */
  String formatSecondValue();
  
  /**
   * Formats the entire difference as a string.
   */
  String formatDifference();
}
