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
package org.webtestingexplorer.driver;

import net.jsourcerer.webdriver.jserrorcollector.JavaScriptError;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * Creates {@link FirefoxDriver}s.
 * 
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
public class FirefoxWebDriverFactory implements WebDriverFactory {

  private final boolean shouldUseProxy;
  private final boolean shouldUseJSErrorCollector;
  private FirefoxProfile profile;
  
  public FirefoxWebDriverFactory() {
    this(true);
  }
  
  public FirefoxWebDriverFactory(boolean shouldUseProxy) {
    this(shouldUseProxy, true);
  }
  
  public FirefoxWebDriverFactory(boolean shouldUseProxy, boolean shouldUseJSErrorCollector) {
    this.shouldUseProxy = shouldUseProxy;
    this.shouldUseJSErrorCollector = shouldUseJSErrorCollector;
  }
  
  @Override
  public void init() throws Exception {
    // Use a custom profile that trusts the cybervillians cert
    // (to use Selenium with the BrowserMob proxy).
    ProfilesIni allProfiles = new ProfilesIni();
    System.setProperty("webdriver.firefox.profile", "webtestingexplorer");
    String browserProfile = System.getProperty("webdriver.firefox.profile");
    profile = allProfiles.getProfile(browserProfile);
    if (profile == null) {
      throw new RuntimeException("It doesn't look like you have a webdriver.firefox.profile. Please look at http://code.google.com/p/webtestingexplorer/wiki/Building");
    }
    profile.setAcceptUntrustedCertificates(true);
    profile.setAssumeUntrustedCertificateIssuer(false);
    
    if (shouldUseJSErrorCollector) {
      // Install JSErrorCollector for JSErrorCollectorOracle to use if installed.
      JavaScriptError.addExtension(profile);
    }
    
    // The following preferences control Firefox's default behavior of sending
    // requests for favicon.ico, which results in lots of bogus 404's for sites
    // that don't have favicons. If people want to use the tool to specifically
    // look for 404's AND they have favicons, perhaps we should make this
    // configurable.
    profile.setPreference("browser.chrome.favicons", false);
    profile.setPreference("browser.chrome.site_icons", false);    
  }

  @Override
  public WebDriver createWebDriver(WebDriverProxy proxy) throws Exception {
    DesiredCapabilities capabilities = DesiredCapabilities.firefox();
    capabilities.setCapability(FirefoxDriver.PROFILE, profile);
    if (shouldUseProxy) {
      capabilities.setCapability(CapabilityType.PROXY, proxy);
    }
    return new FirefoxDriver(capabilities);
  }

  @Override
  public boolean shouldUseProxy() {
    return shouldUseProxy;
  }

  @Override
  public void term() throws Exception {
    // Nothing to do.
  }
}
