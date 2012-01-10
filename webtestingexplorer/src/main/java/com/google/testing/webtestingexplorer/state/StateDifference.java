// Copyright 2012 Google Inc. All Rights Reserved.

package com.google.testing.webtestingexplorer.state;

/**
 * Encapsulates a difference in a subset (for example, a single web element
 * property) of the state.
 * 
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
public class StateDifference {

  private final String property;
  private final Object firstStateValue;
  private final Object secondStateValue;
  
  public StateDifference(String property, Object firstStateValue, Object secondStateValue) {
    super();
    this.property = property;
    this.firstStateValue = firstStateValue;
    this.secondStateValue = secondStateValue;
  }

  public String getProperty() {
    return property;
  }
  
  public Object getFirstStateValue() {
    return firstStateValue;
  }
  
  public Object getSecondStateValue() {
    return secondStateValue;
  }
}
