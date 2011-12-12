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

import com.google.testing.webtestingexplorer.identifiers.IdWebElementIdentifier;
import com.google.testing.webtestingexplorer.identifiers.IndexWebElementIdentifier;
import com.google.testing.webtestingexplorer.identifiers.NameWebElementIdentifier;
import com.google.testing.webtestingexplorer.identifiers.WebElementIdentifier;
import com.google.testing.webtestingexplorer.wait.WaitCondition;

import net.jsourcerer.webdriver.jserrorcollector.JavaScriptError;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Custom wrapper for WebDriver that adds functionality that we find
 * useful.
 * 
 * @author smcmaster@google.com (Scott McMaster)
 */
public class WebDriverWrapper {
	
  private final static Logger LOGGER = Logger.getLogger(WebDriverWrapper.class.getName());
	
  private WebDriver driver;
  private WebDriverProxy proxy;

  /**
   * The identifier of the current frame.
   */
  private String currentFrameIdentifier;
  
  public WebDriverWrapper(WebDriverProxy proxy) throws Exception {
    DesiredCapabilities driverCapabilities = new DesiredCapabilities();
    driverCapabilities.setCapability(CapabilityType.PROXY, proxy.getSeleniumProxy());
    
    // Install JSErrorCollector for JSErrorCollectorOracle to use if installed.
    final FirefoxProfile profile = new FirefoxProfile();
    JavaScriptError.addExtension(profile);
    profile.setProxyPreferences(proxy.getSeleniumProxy());
    
    // The following preferences control Firefox's default behavior of sending
    // requests for favicon.ico, which results in lots of bogus 404's for sites
    // that don't have favicons. If people want to use the tool to specifically
    // look for 404's AND they have favicons, perhaps we should make this
    // configurable.
    profile.setPreference("browser.chrome.favicons", false);
    profile.setPreference("browser.chrome.site_icons", false);
    
    driver = new FirefoxDriver(profile);
    this.proxy = proxy;
  }
  
  public WebDriver getDriver() {
    return driver;
  }
  
  /**
   * Gets the given url and waits for the wait conditions to be satisifed.
   */
  public void get(String url, List<WaitCondition> waitConditions) {
    LOGGER.log(Level.INFO, "Getting " + url);
    proxy.resetForRequest();
    driver.get(url);
    waitOnConditions(waitConditions);
  }

  /**
   * Changes the current frame that the driver is looking at. Subsequent
   * calls to methods on this class will be directed at that frame by
   * WebDriver.
   * TODO(smcmaster): Currently nobody calls this, but we will need it to hook
   * up frame support.
   * 
   * @param frameIdentifier the index to set. 
   */
  public void setCurrentFrame(String frameIdentifier) {
    this.currentFrameIdentifier = frameIdentifier;
    driver.switchTo().frame(this.currentFrameIdentifier);
  }
  
  /**
   * Gets a map of the URI(s) and corresponding status codes for the
   * last get request.
   */
  public Map<URI, Integer> getLastRequestStatusMap() {
    return proxy.getLastRequestStatusMap();
  }
  
  public List<WebElement> getAllElements() {
    return driver.findElements(By.xpath("//*"));
  }
  
  /**
   * Returns a list of all of the elements that we know how to generate
   * actions for.
   */
  public List<WebElement> getActionElements() {
    List<WebElement> elements = new ArrayList<WebElement>();
    elements.addAll(driver.findElements(By.xpath("//input")));
    elements.addAll(driver.findElements(By.xpath("//textarea")));
    elements.addAll(driver.findElements(By.xpath("//a")));
    elements.addAll(driver.findElements(By.xpath("//button")));
    elements.addAll(driver.findElements(By.xpath("//select")));
    return elements;
  }
  
  /*
   * Generate identifier for a WebElement.
   */
  public WebElementIdentifier generateIdentifier(int elementIndex, WebElement element) {
    String name = element.getAttribute("name");
    String id = element.getAttribute("id");

    WebElementIdentifier identifier;
    if (id != null && id.length() > 0) {
      identifier = new IdWebElementIdentifier(id);
    } else if (name != null && name.length() > 0) {
      identifier = new NameWebElementIdentifier(name);
    } else {
      identifier = new IndexWebElementIdentifier(elementIndex);
    }

    identifier.setFrameIdentifier(currentFrameIdentifier);
    return identifier;
  }

  /**
   * Returns a list of all visible elements.
   */
  public Map<Integer, WebElement> getVisibleElements() {
	  List<WebElement> allElements = new ArrayList<WebElement>();
	  allElements.addAll(this.getAllElements());
	  Map<Integer, WebElement> visibleElements = new HashMap<Integer, WebElement>();
	  
	  WebElement e;
	  for (int i = 0; i < allElements.size(); ++i) {
		  e = allElements.get(i);
	    if (e.isDisplayed()) {
		    visibleElements.put(new Integer(i), e);
	    }
	  }
	  return visibleElements;
  }
  
  /**
   * Waits for the given list of conditions to be true before returning.
   * TODO(smcmaster): Make the wait between checks configurable, and add a timeout.
   */
  public void waitOnConditions(List<WaitCondition> waitConditions) {
    if (waitConditions == null) {
      return;
    }
    
    for (WaitCondition waitCondition : waitConditions) {
      waitCondition.reset();
    }
    while (true) {
      boolean allCanContinue = true;
      String conditionDescription = "";
      for (WaitCondition waitCondition : waitConditions) {
        if (!waitCondition.canContinue(this)) {
          allCanContinue = false;
          conditionDescription = waitCondition.getDescription();
          break;
        }
      }
      if (allCanContinue) {
        break;
      }
      LOGGER.log(Level.INFO, "Waiting for " + conditionDescription);
      try {
        Thread.sleep(1000);
      } catch (InterruptedException useless) {
      }
    }
  }

  public void close() {
    driver.close();
  }
}
