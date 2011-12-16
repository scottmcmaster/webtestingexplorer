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

import com.google.common.collect.Lists;
import com.google.testing.webtestingexplorer.identifiers.IdWebElementIdentifier;
import com.google.testing.webtestingexplorer.identifiers.IndexWebElementIdentifier;
import com.google.testing.webtestingexplorer.identifiers.NameWebElementIdentifier;
import com.google.testing.webtestingexplorer.identifiers.WebElementIdentifier;
import com.google.testing.webtestingexplorer.identifiers.WebElementWithIdentifier;
import com.google.testing.webtestingexplorer.javascript.JavaScriptUtil;
import com.google.testing.webtestingexplorer.wait.WaitCondition;

import net.jsourcerer.webdriver.jserrorcollector.JavaScriptError;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URI;
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
  private String currentFrameIdentifier = null;
  
  public WebDriverWrapper(WebDriverProxy proxy) throws Exception {
    DesiredCapabilities driverCapabilities = new DesiredCapabilities();
    driverCapabilities.setCapability(CapabilityType.PROXY, proxy.getSeleniumProxy());
    
    // Use a custom profile that trusts the cybervillians cert
    // (to use Selenium with the BrowserMob proxy).
    ProfilesIni allProfiles = new ProfilesIni();
    System.setProperty("webdriver.firefox.profile","webtestingexplorer");
    String browserProfile = System.getProperty("webdriver.firefox.profile");
    FirefoxProfile profile = allProfiles.getProfile(browserProfile); 
    profile.setAcceptUntrustedCertificates(true);
    profile.setAssumeUntrustedCertificateIssuer(false);

    // Install JSErrorCollector for JSErrorCollectorOracle to use if installed.
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
    driver.switchTo().defaultContent();
    currentFrameIdentifier = null;
    driver.get(url);
    waitOnConditions(waitConditions);
  }

  /**
   * Changes the current frame that the driver is looking at. Subsequent
   * calls to methods on this class will be directed at that frame by
   * WebDriver.
   * 
   * @param frameIdentifier the index to set, or null to go to the default
   *     content.
   */
  public void setCurrentFrame(String frameIdentifier) {
    this.currentFrameIdentifier = frameIdentifier;
    if (this.currentFrameIdentifier != null) {
      driver.switchTo().frame(this.currentFrameIdentifier);
    } else {
      driver.switchTo().defaultContent();
    }
  }
  
  /**
   * Gets a map of the URI(s) and corresponding status codes for the
   * last get request.
   */
  public Map<URI, Integer> getLastRequestStatusMap() {
    return proxy.getLastRequestStatusMap();
  }
  
  /**
   * Gets all elements in the current browser, across frames.
   * TODO(smcmaster): This doesn't work because apparently in WebDriver you cannot
   * hold onto elements across active frames without them getting stale.
   * Probably we need to rework all of this to return just the identifiers, not the
   * elements, and make the callers use the identifiers to get the elements
   * just-in-time.
   */
  public List<WebElementWithIdentifier> getAllElements() {
    List<WebElementWithIdentifier> allElements = Lists.newArrayList();
    getAllElementsForFrame(null, 0, allElements);
    return allElements;
  }
  
  /**
   * Recursive-helper to go across frames for getAllElements. Adds to the 
   * given list of elements.
   */
  private void getAllElementsForFrame(String frameIdentifier,
      int startElementIndex, List<WebElementWithIdentifier> allElements) {
    
    String oldCurrentFrameIdentifier = currentFrameIdentifier;

    setCurrentFrame(frameIdentifier);
    
    List<WebElement> frameElements = driver.findElements(By.xpath("//*"));
    List<WebElementWithIdentifier> frameElementsWithIds = Lists.newArrayList();
    for (WebElement element : frameElements) {
      frameElementsWithIds.add(new WebElementWithIdentifier(element,
          generateIdentifier(startElementIndex++, element)));
    }
    
    for (WebElementWithIdentifier elementAndId : frameElementsWithIds) {
      WebElement element = elementAndId.getElement();
      if ("frame".equals(element.getTagName().toLowerCase()) ||
          "iframe".equals(element.getTagName().toLowerCase())) {
        String nameOrId = element.getAttribute("name");
        if (nameOrId == null) {
          nameOrId = element.getAttribute("id");
        }
        if (nameOrId != null) {
          getAllElementsForFrame(nameOrId, startElementIndex, allElements);
        }
      }
    }
    allElements.addAll(frameElementsWithIds);
    setCurrentFrame(oldCurrentFrameIdentifier);
  }
  
  /**
   * Returns a list of all of the elements that we know how to generate
   * actions for.
   * Currently this is not used, and it's not clear to me whether we would
   * want this to work on the current frame or across the whole document. 
  public List<WebElement> getActionElements() {
    List<WebElement> elements = new ArrayList<WebElement>();
    elements.addAll(driver.findElements(By.xpath("//input")));
    elements.addAll(driver.findElements(By.xpath("//textarea")));
    elements.addAll(driver.findElements(By.xpath("//a")));
    elements.addAll(driver.findElements(By.xpath("//button")));
    elements.addAll(driver.findElements(By.xpath("//select")));
    return elements;
  }
  */
  
  /**
   * Generate identifier for a WebElement.
   */
  private WebElementIdentifier generateIdentifier(int elementIndex, WebElement element) {
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
  public List<WebElementWithIdentifier> getVisibleElements() {
	  List<WebElementWithIdentifier> allElements = getAllElements();
	  List<WebElementWithIdentifier> visibleElements = Lists.newArrayList();
	  
	  for (WebElementWithIdentifier elementWithId : allElements) {
		WebElement element = elementWithId.getElement();
	    if (element.isDisplayed()) {
		   visibleElements.add(elementWithId);
	    }
	  }
	  return visibleElements;
  }
  
  /**
   * Get given properties of all elements in DOM.
   */
  public String getAllElementsProperties(List<String> properties) {
  	StringBuilder jsString = new StringBuilder();
    jsString.append(JavaScriptUtil.generateJavaScriptMap(properties));

    jsString.append(JavaScriptUtil.getJavaScriptFromFile("/getAllElementsProperties.js"));

    // Execute js
    JavascriptExecutor js = (JavascriptExecutor) driver;
    return (String) js.executeScript(jsString.toString());
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
