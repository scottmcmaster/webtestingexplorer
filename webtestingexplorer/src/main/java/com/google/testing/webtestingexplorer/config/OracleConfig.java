// Copyright 2011 Google Inc. All Rights Reserved.

package com.google.testing.webtestingexplorer.config;

import com.google.testing.webtestingexplorer.oracles.Oracle;

import java.util.ArrayList;
import java.util.List;

/**
 * A collection of different types of {@link Oracle}s.
 * 
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
public class OracleConfig {
  
  /**
   * A list of oracles that get checked after each action is performed.
   */
  private List<Oracle> afterActionOracles = new ArrayList<Oracle>();
  
  /**
   * A list of oracles that get checked after a complete action sequence has been
   * executed.
   */
  private List<Oracle> finalOracles = new ArrayList<Oracle>();
  
  public List<Oracle> getAfterActionOracles() {
    return afterActionOracles;
  }

  public OracleConfig addAfterActionOracle(Oracle oracle) {
    afterActionOracles.add(oracle);
    return this;
  }

  public List<Oracle> getFinalOracles() {
    return finalOracles;
  }

  public OracleConfig addFinalOracle(Oracle oracle) {
    finalOracles.add(oracle);
    return this;
  }
}
