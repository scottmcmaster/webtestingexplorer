/*
 * Copyright 2011 Google Inc. All Rights Reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.testing.webtestingexplorer.state;

import com.google.testing.webtestingexplorer.identifiers.WebElementIdentifier;
import com.google.testing.webtestingexplorer.identifiers.XpathWebElementIdentifier;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * State contains given set of information on all elements.
 * 
 * @author xyuan@google.com (Xun Yuan)
 */
public class CustomizedPropertiesElementsState extends ElementsState {
  List<String> customizedProperties;

  public CustomizedPropertiesElementsState(List<String> properties, String xmlString) {
    elementType = ElementType.ALL;
    if (properties != null) {
      customizedProperties = new ArrayList<String>(properties);
    } else {
      customizedProperties = new ArrayList<String>();
    }
    elementProperties = parseXML(xmlString);
  }

  private Map<WebElementIdentifier, Map<String, String>> parseXML(String xmlString) {
    Map<WebElementIdentifier, Map<String, String>> elementProperties =
        new HashMap<WebElementIdentifier, Map<String, String>>();
    XpathWebElementIdentifier identifier;
    Map<String, String> attributeMap;

    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    Document doc = null;

    try {
      DocumentBuilder db = dbf.newDocumentBuilder();
      InputSource is = new InputSource();
      is.setCharacterStream(new StringReader(xmlString));

      doc = db.parse(is);
    } catch (ParserConfigurationException pce) {
      pce.printStackTrace();
    } catch (SAXException se) {
      se.printStackTrace();
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }

    if (doc == null) {
      return elementProperties;
    }

    NodeList nl = doc.getElementsByTagName("element");
    if (nl != null && nl.getLength() > 0) {
      // Loop through all elements
      for (int i = 0; i < nl.getLength(); ++i) {
        identifier = null;
        attributeMap = null;
        Element el = (Element) nl.item(i);

        // Get xpath
        NodeList xpaths = el.getElementsByTagName("xpath");
        Element xpathElement = (Element) xpaths.item(0);
        String xpath = getCharacterDataFromElement(xpathElement);
        identifier = new XpathWebElementIdentifier(xpath);

        NodeList attributes = el.getElementsByTagName("attribute");
        if (attributes != null && attributes.getLength() > 0) {
          // Loop through all attributes
          attributeMap = new HashMap<String, String>();
          for (int j = 0; j < attributes.getLength(); ++j) {
            Element attributeElement = (Element) attributes.item(j);
            NamedNodeMap attributeNodeMap = attributeElement.getAttributes();
            for (int m = 0; m < attributeNodeMap.getLength(); ++m) {
              Node attributeNode = attributeNodeMap.item(m);
              attributeMap.put(attributeNode.getNodeName(), attributeNode.getNodeValue());
            }
          }// for
        }

        if (identifier != null && attributeMap != null) {
          elementProperties.put(identifier, attributeMap);
        }
      } // for
    }
    return elementProperties;
  }

  private String getCharacterDataFromElement(Element e) {
    Node child = e.getFirstChild();
    if (child instanceof CharacterData) {
      CharacterData cd = (CharacterData) child;
      return cd.getData();
    }
    return "";
  }

  @Override
  public StateChecker createStateChecker() {
    return new CustomizedPropertiesElementsStateChecker(customizedProperties);
  }
}
