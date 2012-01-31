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
package com.google.testing.webtestingexplorer.driver;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.RequestWrapper;
import org.apache.http.protocol.HttpContext;
import org.browsermob.proxy.ProxyServer;
import org.openqa.selenium.Proxy;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A proxy for our WebDriver requests since WebDriver omits some very useful
 * features (like the ability to capture http status codes).
 * 
 * @author smcmaster@google.com (Scott McMaster)
 */
public class WebDriverProxy {

  private final static Logger LOGGER = Logger.getLogger(WebDriverProxy.class.getName());

  /**
   * The amount of time we wait between checks that each request has a matching
   * response.
   */
  private long responseWaitIntervalMillis = 1000;

  /**
   * The amount of time we wait in total for all responses to come back.
   */
  private long responseWaitTimeoutMillis = 30000;
  
  private Proxy proxy;
  private List<URI> requestURIs = new ArrayList<URI>();
  private List<Integer> statusCodes = new ArrayList<Integer>();

  private ProxyServer proxyServer;
  
  public WebDriverProxy() throws Exception {
    proxyServer = new ProxyServer(4444);
    proxyServer.start();

    proxyServer.addRequestInterceptor(new HttpRequestInterceptor() {
      @SuppressWarnings("unused") // exception spec
      @Override
      public void process(HttpRequest request, HttpContext context)
          throws HttpException, IOException {
        RequestWrapper wrapper = (RequestWrapper) request;
        if (wrapper.getOriginal() instanceof HttpGet) {
          HttpGet getRequest = (HttpGet) wrapper.getOriginal();
          requestURIs.add(getRequest.getURI());
        } else {
          requestURIs.add(wrapper.getURI());
        }
      }
    });
    
    proxyServer.addResponseInterceptor(new HttpResponseInterceptor() {      
      @SuppressWarnings("unused") // exception spec
      @Override
      public void process(HttpResponse response, HttpContext context)
          throws HttpException, IOException {
        statusCodes.add(response.getStatusLine().getStatusCode());
      }
    });
    
    // Stash the Selenium proxy object.
    proxy = proxyServer.seleniumProxy();
  }

  /**
   * Shuts down the proxy we started, thereby freeing up the port.
   */
  public void stop() {
    try {
      proxyServer.stop();
    } catch (Exception useless) {
      LOGGER.warning("Failed to nicely stop the proxy server");
    }
  }

  public void setResponseWaitIntervalMillis(long responseWaitIntervalMillis) {
    this.responseWaitIntervalMillis = responseWaitIntervalMillis;
  }

  public void setResponseWaitTimeoutMillis(long responseWaitTimeoutMillis) {
    this.responseWaitTimeoutMillis = responseWaitTimeoutMillis;
  }

  /**
   * @return the Selenium proxy.
   */
  public Proxy getSeleniumProxy() {
    return proxy;
  }
  
  public int getRequestCount() {
    return requestURIs.size();
  }
  
  public int getResponseCount() {
    return statusCodes.size();
  }
  
  public void resetForRequest() {
    requestURIs = new ArrayList<URI>();
    statusCodes = new ArrayList<Integer>();
  }

  public Map<URI, Integer> getLastRequestStatusMap() {
    Map<URI, Integer> statusMap = new HashMap<URI, Integer>();
    // If something isn't back yet, wait a short time for it.
    long startMillis = System.currentTimeMillis();
    while (requestURIs.size() != statusCodes.size()) {
      try {
        LOGGER.log(Level.INFO, "Waiting for all requests to return");
        Thread.sleep(responseWaitIntervalMillis);
      } catch (InterruptedException useless) { }
      
      if (System.currentTimeMillis() - startMillis > responseWaitTimeoutMillis) {
        LOGGER.log(Level.WARNING, "Timeout waiting for responses");
        break;
      }
    }
    
    for (int i = 0; i < requestURIs.size(); i++) {
      Integer statusCode = null;
      if (i < statusCodes.size()) {
        statusCode = statusCodes.get(i);
      }
      statusMap.put(requestURIs.get(i), statusCode);
    }
    return statusMap;
  }
}
