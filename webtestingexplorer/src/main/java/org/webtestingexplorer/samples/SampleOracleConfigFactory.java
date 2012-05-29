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

package org.webtestingexplorer.samples;

import org.webtestingexplorer.config.OracleConfig;
import org.webtestingexplorer.config.OracleConfigFactory;
import org.webtestingexplorer.oracles.HttpStatusCodeOracle;
import org.webtestingexplorer.oracles.JSErrorCollectorOracle;

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
