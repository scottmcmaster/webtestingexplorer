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

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;

/**
 * Creates {@link ChromeDriver}s.
 * 
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
public class ChromeWebDriverFactory implements WebDriverFactory {

  private ChromeDriverService driverService;
  
  @Override
  public WebDriver createWebDriver(WebDriverProxy proxy) throws Exception {
    DesiredCapabilities driverCapabilities = DesiredCapabilities.chrome();
    if (proxy != null) {
      driverCapabilities.setCapability(CapabilityType.PROXY, proxy.getSeleniumProxy());
    }
    return new RemoteWebDriver(driverService.getUrl(), driverCapabilities);
  }

  @Override
  public boolean shouldUseProxy() {
    return false;
  }

  @Override
  public void init() throws Exception {
    driverService = new ChromeDriverService.Builder()
      .usingDriverExecutable(new File(System.getProperty("webdriver.chrome.driver")))
      .usingAnyFreePort()
      .build();
    driverService.start();
  }

  @Override
  public void term() throws Exception {
    driverService.stop();
  }
}
