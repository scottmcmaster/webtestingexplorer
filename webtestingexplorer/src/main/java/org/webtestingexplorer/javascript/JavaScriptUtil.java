package org.webtestingexplorer.javascript;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class JavaScriptUtil {
	public static String generateJavaScriptMap(List<String> properties) {
		StringBuilder jsString = new StringBuilder();
		jsString.append("var propertiesMap = new Object(); ");
    if (properties != null && properties.size() == 0) {   	
    	for (String property: properties) {
      	jsString.append("propertiesMap['" + property + "'] = ' ';");
      }
    }
    return jsString.toString();   
	}
	
	public static String getJavaScriptFromFile(String file) {
		StringBuilder jsString = new StringBuilder();
		try{
			InputStream in = JavaScriptUtil.class.getResourceAsStream(file);
		  // Get the object of DataInputStream
		  DataInputStream dataIn = new DataInputStream(in);
		  BufferedReader br = new BufferedReader(new InputStreamReader(dataIn));
		  String strLine;
		  //Read File Line By Line
		  while ((strLine = br.readLine()) != null)   {
		    jsString.append(strLine);
		  }
		  in.close();
		}catch (Exception e){//Catch exception if any
		  System.err.println("Error: " + e.getMessage());
		}
		return jsString.toString();
	}
}
