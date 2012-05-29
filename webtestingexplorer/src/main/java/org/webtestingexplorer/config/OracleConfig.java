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

package org.webtestingexplorer.config;

import org.webtestingexplorer.oracles.Oracle;

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
