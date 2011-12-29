// Copyright 2011 Google Inc. All Rights Reserved.

package com.google.testing.webtestingexplorer.samples;

import com.google.testing.webtestingexplorer.config.OracleConfig;
import com.google.testing.webtestingexplorer.config.OracleConfigFactory;
import com.google.testing.webtestingexplorer.oracles.HttpStatusCodeOracle;
import com.google.testing.webtestingexplorer.oracles.JSErrorCollectorOracle;

/**
 * An {@link OracleConfigFactory} for the sample main program.
 * 
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
public class SampleOracleConfigFactory implements OracleConfigFactory {

  @Override
  public OracleConfig createOracleConfig() {
    OracleConfig oracleConfig = new OracleConfig()
        .addAfterActionOracle(new HttpStatusCodeOracle().setDisallowedStatusCodes(500, 503))
        .addAfterActionOracle(new JSErrorCollectorOracle());
    return oracleConfig;
  }
}
