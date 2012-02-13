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

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.testing.webtestingexplorer.identifiers.IdWebElementIdentifier;
import com.google.testing.webtestingexplorer.identifiers.IndexWebElementIdentifier;
import com.google.testing.webtestingexplorer.identifiers.NameWebElementIdentifier;
import com.google.testing.webtestingexplorer.identifiers.WebElementIdentifier;
import com.google.testing.webtestingexplorer.identifiers.WebElementWithIdentifier;
import com.google.testing.webtestingexplorer.javascript.JavaScriptUtil;
import com.google.testing.webtestingexplorer.wait.WaitCondition;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
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
  private long waitIntervalMillis;
  private long waitTimeoutMillis;  
  private boolean useElementsCache;
  
  /**
   * A cache of the results of getAllElements(ForFrame).
   */
  private List<WebElementWithIdentifier> elementsCache;
  
  /**
   * The last frame we loaded elements for; used for caching.
   */
  private String lastFrameIdentifier;
  
  public WebDriverWrapper(WebDriverFactory driverFactory, WebDriverProxy proxy,
      long waitIntervalMillis, long waitTimeoutMillis, boolean useElementsCache)
          throws Exception {
    driver = driverFactory.createWebDriver(proxy);
    this.waitIntervalMillis = waitIntervalMillis;
    this.waitTimeoutMillis = waitTimeoutMillis;
    this.proxy = proxy;
    this.useElementsCache = useElementsCache;
  }
  
  public WebDriver getDriver() {
    return driver;
  }
  
  /**
   * @return whether or not the driver is configured to use a proxy.
   */
  public boolean isUsingProxy() {
    return (proxy != null);
  }
  
  public void setUseElementsCache(boolean useElementsCache) {
    this.useElementsCache = useElementsCache;
  }

  /**
   * Gets the given url and waits for the wait conditions to be satisifed.
   */
  public void get(String url, List<WaitCondition> waitConditions) {
    LOGGER.log(Level.INFO, "Getting " + url);
    invalidateElementsCache();
    if (proxy != null) {
      proxy.resetForRequest();
    }
    driver.switchTo().defaultContent();
    driver.get(url);
    waitOnConditions(waitConditions);
  }

  /**
   * Finds an element in the given frame and leaves the frame as such
   * so that we can work with the element without stale-element exceptions.
   */
  public WebElement findElementInFrame(By by, String frameIdentifier) {
    List<WebElement> elements = findElementsInFrame(by, frameIdentifier);
    if (elements.size() > 1) {
      LOGGER.warning("Found multiple elements for " + by + ", returning the first one");
    }
    return elements.get(0);
  }

  /**
   * Finds multiple elements in the given frame and leaves the frame as such
   * so that we can work with the element without stale-element exceptions.
   */
  public List<WebElement> findElementsInFrame(By by, String frameIdentifier) {
    LOGGER.log(Level.FINE, "Looking for elements " + by + " in frame " + frameIdentifier);
    switchToFrame(frameIdentifier);
    return driver.findElements(by);
  }

  /**
   * Helper method that switches to a frame, handling a null as the default content.
   * TODO(smcmaster): Currently this only works for one level of frames.
   */
  private void switchToFrame(String frameIdentifier) {
    if (Objects.equal(frameIdentifier, lastFrameIdentifier)) {
      return;
    }
    
    driver.switchTo().defaultContent();
    if (frameIdentifier != null) {
      driver.switchTo().frame(frameIdentifier);
    }
  }

  /**
   * Gets a map of the URI(s) and corresponding status codes for the
   * last get request.
   */
  public Map<URI, Integer> getLastRequestStatusMap() {
    if (proxy == null) {
      return Maps.newHashMap();
    }
    return proxy.getLastRequestStatusMap();
  }
  
  public WebDriverProxy getProxy() {
    return proxy;
  }
  
  /**
   * Clears out any cached lists of {@link WebElementIdentifier}s.
   */
  public void invalidateElementsCache() {
    elementsCache = null;
    lastFrameIdentifier = null;
  }
  
  /**
   * Gets all elements in the current browser, for a single frame.
   */
  public List<WebElementWithIdentifier> getAllElementsForFrame(String frameIdentifier) {
    if (Objects.equal(frameIdentifier, lastFrameIdentifier) && validateElementsCache()) {
      LOGGER.log(Level.FINE, "Using cached elements for frame " + frameIdentifier);
      return elementsCache;
    }
    LOGGER.log(Level.FINE, "Getting all elements for frame " + frameIdentifier);

    List<WebElementWithIdentifier> allElements = Lists.newArrayList();
    getAllElementsForFrameHelper(frameIdentifier, allElements);
    lastFrameIdentifier = frameIdentifier;
    if (useElementsCache) {
      elementsCache = allElements;
    }
    return allElements;
  }
  
  /**
   * Gets all elements in the current browser, across frames.
   */
  public List<WebElementWithIdentifier> getAllElements() {
    if (lastFrameIdentifier == null && validateElementsCache()) {
      LOGGER.log(Level.FINE, "Using cached elements");
      return elementsCache;
    }
    LOGGER.log(Level.FINE, "Getting all elements");
    
    List<WebElementWithIdentifier> allElements = Lists.newArrayList();
    getAllElementsForFrameHelper(null, allElements);
    lastFrameIdentifier = null;
    if (useElementsCache) {
      elementsCache = allElements;
    }
    getDriver().switchTo().defaultContent();
    return allElements;
  }
  
  private boolean validateElementsCache() {
    if (elementsCache == null || elementsCache.size() == 0) {
      return false;
    }
    try {
      // The next call will trigger stale-element if we are stale.
      elementsCache.get(0).getElement().getTagName();
      return true;
    } catch (StaleElementReferenceException e) {
      return false;
    }
  }
  /**
   * Recursive-helper to go across frames for getAllElements. Adds to the 
   * given list of elements.
   */
  private void getAllElementsForFrameHelper(String frameIdentifier,
      List<WebElementWithIdentifier> allElements) {
    
    switchToFrame(frameIdentifier);
    
    int startElementIndex = 0;
    List<WebElement> frameElements = driver.findElements(By.xpath("//*"));
    List<WebElementWithIdentifier> frameElementsWithIds = Lists.newArrayList();
    List<WebElement> childFrames = Lists.newArrayList();
    
    for (WebElement element : frameElements) {
      try {
        // Filter out some elements we don't take actions on.
        if (!isActionable(element)) {
          continue;
        }
        
        // Save frames to use later.
        if (isFrame(element)) {
          childFrames.add(element);
          continue;
        }
        
        frameElementsWithIds.add(new WebElementWithIdentifier(element,
            generateIdentifier(startElementIndex++, element, frameIdentifier)));
      } catch (Exception e) {
        LOGGER.log(Level.SEVERE, "Exception evaluating element", e);
        throw new RuntimeException(e);
      }
    }
    allElements.addAll(frameElementsWithIds);
    

    // Iterate over the child frames.
    List<String> childFrameIdentifiers = Lists.newArrayList();
    for (WebElement childFrame : childFrames) {
      String nameOrId = calculateFrameIdentifier(childFrame);
      if (nameOrId != null) {
        childFrameIdentifiers.add(nameOrId);
      }
    }
    
    // Now do all the child frames.
    for (String childFrameIdentifier : childFrameIdentifiers) {
      getAllElementsForFrameHelper(childFrameIdentifier, allElements);
      // Switch back so that the next frame id is resolved relative to the correct location.
      switchToFrame(frameIdentifier);
    }
  }

  /**
   * @return whether the given element is a frame or not.
   */
  private boolean isFrame(WebElement element) {
    String tagName = element.getTagName().toLowerCase();
    if ("frame".equals(tagName) ||
        "iframe".equals(tagName)) {
      return true;
    }
    return false;
  }
  
  /**
   * @return whether or not any action might make sense on this element.
   */
  private boolean isActionable(WebElement element) {
    String tagName = element.getTagName().toLowerCase();
    if ("meta".equals(tagName) ||
        "script".equals(tagName) ||
        "title".equals(tagName) ||
        "style".equals(tagName) ||
        "html".equals(tagName) ||
        "head".equals(tagName) ||
        "body".equals(tagName)) {
      return false;
    }
    return true;
  }
  
  /**
   * Figures out the best frame identifier to use for the given element.
   * 
   * @param element assumed to be a frame or iframe.
   * @return the identifier, which theoretically may be null.
   */
  private String calculateFrameIdentifier(WebElement element) {
    String nameOrId = element.getAttribute("name");
    if (nameOrId == null) {
      nameOrId = element.getAttribute("id");
    }
    return nameOrId;
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
  private WebElementIdentifier generateIdentifier(int elementIndex, WebElement element,
      String frameIdentifier) {
    String name = element.getAttribute("name");
    String id = element.getAttribute("id");
    String tagName = element.getTagName();
    
    WebElementIdentifier identifier;
    if (id != null && id.length() > 0) {
      identifier = new IdWebElementIdentifier(id, frameIdentifier, tagName);
    } else if (name != null && name.length() > 0) {
      identifier = new NameWebElementIdentifier(name, frameIdentifier, tagName);
    } else {
      identifier = new IndexWebElementIdentifier(elementIndex, frameIdentifier, tagName);
    }

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
   */
  public void waitOnConditions(List<WaitCondition> waitConditions) {
    if (waitConditions == null) {
      return;
    }
    
    for (WaitCondition waitCondition : waitConditions) {
      waitCondition.reset();
    }
    long startMillis = System.currentTimeMillis();
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
      
      if (System.currentTimeMillis() - startMillis > waitTimeoutMillis) {
        LOGGER.log(Level.WARNING, "Timeout waiting for wait conditions");
        break;
      }
      
      LOGGER.log(Level.INFO, "Waiting for " + conditionDescription);
      try {
        Thread.sleep(waitIntervalMillis);
      } catch (InterruptedException useless) {
      }
    }
  }

  public void close() {
    for (String windowHandle : driver.getWindowHandles()) {
      driver.switchTo().window(windowHandle);
      driver.close();
    }
    
    // Work around crazy WebDriver bug described here:
    // http://code.google.com/p/selenium/issues/detail?id=1934.
    List<String> profileDirs = Lists.newArrayList("anonymous*webdriver-profile",
        "userprofile*copy", "seleniumSslSupport*");
    File tmpDir = new File(System.getProperty("java.io.tmpdir"));
    FilenameFilter profileDirsFilter = new WildcardFileFilter(profileDirs);
    File[] files = tmpDir.listFiles(profileDirsFilter);
    for (File profileDir : files) {
      LOGGER.info("Cleaning up tmp profile directory: " + profileDir.getAbsolutePath());
      try {
        FileUtils.deleteDirectory(profileDir);
      } catch (IOException e) {
        LOGGER.log(Level.WARNING,
            "Failed to delete tmp profile directory: " + profileDir.getAbsolutePath(), e);
      }
    }
  }
}
