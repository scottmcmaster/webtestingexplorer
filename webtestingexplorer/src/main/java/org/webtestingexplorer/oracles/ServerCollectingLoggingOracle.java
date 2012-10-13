package org.webtestingexplorer.oracles;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.webtestingexplorer.driver.WebDriverWrapper;

import com.google.common.collect.Lists;

/**
 * Calls the {@link org.webtestingexplorer.server.servlets.CollectingLoggingServlet}
 * to get the state of log messages since it was last reset, looks for errors.
 * 
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
public class ServerCollectingLoggingOracle implements Oracle {

  private final static Logger LOGGER = Logger.getLogger(ServerCollectingLoggingOracle.class.getName());

	private final String logMessageEndpointUrl;
	
	public ServerCollectingLoggingOracle(String logMessageEndpointUrl) {
		this.logMessageEndpointUrl = logMessageEndpointUrl;
	}
	
	@Override
	public void reset() {
		// The server side resets on any post.
    HttpClient httpclient = new DefaultHttpClient();
    HttpPost httppost = new HttpPost(logMessageEndpointUrl);
    try {
			httpclient.execute(httppost);
		} catch (ClientProtocolException e) {
			LOGGER.log(Level.SEVERE, "Exception resetting log message endpoint", e);
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Exception resetting log message endpoint", e);
		}
	}

	@Override
	public List<FailureReason> check(WebDriverWrapper driver) {

    HttpClient httpclient = new DefaultHttpClient();
    HttpGet httpget = new HttpGet(logMessageEndpointUrl);
    String json = null;
		try {
			HttpResponse response = httpclient.execute(httpget);
	    HttpEntity entity = response.getEntity();
	    json = IOUtils.toString(entity.getContent());
		} catch (ClientProtocolException e) {
			LOGGER.log(Level.SEVERE, "Exception retrieving JSON log messages", e);
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Exception retrieving JSON log messages", e);
		}
    
		return processResults(json);
	}
	
	private List<FailureReason> processResults(String json) {
		List<FailureReason> result = Lists.newArrayList();
		if (json == null || json.isEmpty()) {
			return result;
		}
		
		try {
			JSONArray logMessageArray = new JSONArray(json);
			for (int i = 0; i < logMessageArray.length(); i++) {
				JSONObject logMessage = logMessageArray.getJSONObject(i);
				String level = logMessage.getString("message");
				if ("SEVERE".equals(level) || "WARNING".equals(level)) {
					String message = logMessage.getString("message");
					String loggerName = logMessage.getString("loggerName");
					long millis = logMessage.getLong("millis");
					
					String fullMessage = "Log Message Failure: " + level + ": " + message + " from " + loggerName + " @" + millis;
					FailureReason failure = new FailureReason(fullMessage);
					result.add(failure);
				}
			}
		} catch (JSONException e) {
			LOGGER.log(Level.SEVERE, "Exception processing JSON log messages", e);
		}
		
		return result;
	}
}
