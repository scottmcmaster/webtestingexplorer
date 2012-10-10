package org.webtestingexplorer.server.servlets;

import java.io.IOException;
import java.util.logging.LogRecord;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.webtestingexplorer.server.oraclesupport.CollectingLoggingHandler;

/**
 * A servlet that exposes the logging information captured by the
 * {@link CollectingLoggingHandler} to the webtestingexplorer
 * client.
 * 
 * NOTE that you would not want to expose an endpoint like this
 * in a production web application.
 * 
 * NOTE ALSO that this is currently very much proof-of-concept.
 * 
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
public class CollectingLoggingServlet extends HttpServlet {

  private static final long serialVersionUID = 6436886362958341997L;

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    JSONArray state = new JSONArray();
    for (LogRecord logRecord : CollectingLoggingHandler.getInstance().getCurrentLogRecords()) {
    	state.put(new JSONObject(logRecord));
    }
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    try {
      state.write(response.getWriter());
    } catch (JSONException e) {
      throw new ServletException(e);
    }
  }
  
  /**
   * Any post will reset the state.
   */
  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
  		throws ServletException, IOException {
  	CollectingLoggingHandler.getInstance().clear();
  }
}
