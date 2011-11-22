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
package com.google.testing.webtestingexplorer.oracles;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.testing.webtestingexplorer.driver.WebDriverWrapper;

/**
 * Flags failures based on allowed/disallowed http status codes.
 * 
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
public class HttpStatusCodeOracle implements Oracle {
  
  /**
   * If non-empty, the set of status codes that any response MUST
   * be in.
   */
  private final Set<Integer> allowedStatusCodes;
  
  /**
   * If non-empty, the set of status codes that will trigger a
   * failure.
   */
  private final Set<Integer> disallowedStatusCodes;
  
  public HttpStatusCodeOracle() {
    allowedStatusCodes = new HashSet<Integer>();
    disallowedStatusCodes = new HashSet<Integer>();
  }
  
  public HttpStatusCodeOracle setAllowedStatusCodes(Integer...codes) {
    for (Integer code : codes) {
      allowedStatusCodes.add(code);
    }
    return this;
  }
  
  public HttpStatusCodeOracle setDisallowedStatusCodes(Integer...codes) {
    for (Integer code : codes) {
      disallowedStatusCodes.add(code);
    }
    return this;
  }
  
  @Override
  public List<FailureReason> check(WebDriverWrapper driver) {
    List<FailureReason> result = new ArrayList<FailureReason>();
    Map<URI, Integer> statusCodes = driver.getLastRequestStatusMap();
    for (Map.Entry<URI, Integer> entry : statusCodes.entrySet()) {
      URI uri = entry.getKey();
      int statusCode = entry.getValue();
      if (!allowedStatusCodes.isEmpty()) {
        // Make sure this code is allowed.
        if (!allowedStatusCodes.contains(statusCodes)) {
          String message = "HTTP " + statusCode + " for " + uri.toString() + 
              " not allowed";
          FailureReason failureReason = new FailureReason(message);
          result.add(failureReason);
        }
        
        if (!disallowedStatusCodes.isEmpty()) {
          // Make sure this code is not disallowed.
          if (disallowedStatusCodes.contains(statusCode)) {
            String message = "HTTP " + statusCode + " for " + uri.toString() +
                " disallowed";
            FailureReason failureReason = new FailureReason(message);
            result.add(failureReason);
          }
        }
      }
    }
    return result;
  }

}
