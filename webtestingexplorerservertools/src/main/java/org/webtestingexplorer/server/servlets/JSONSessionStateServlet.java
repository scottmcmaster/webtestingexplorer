package org.webtestingexplorer.server.servlets;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A servlet that exposes simple session state as a JSONObject,
 * suitable for use with webtestingexplorer's JSONObjectState.
 * Note that to get this to work properly, the XHR to retrieve the
 * state needs to be made from the context of the browser running the
 * application-under-test. This can be done via a call to
 * WebDriver.executeScript(), and the result passed by the state checker
 * to a new JSONObjectState instance.
 * 
 * NOTE ALSO that you would not want to expose an endpoint like this
 * in a production web application.
 * 
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
public class JSONSessionStateServlet extends HttpServlet {

  private static final long serialVersionUID = 6436886362958341997L;

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    JSONObject state = new JSONObject();
    @SuppressWarnings("unchecked")
    Enumeration<String> namesEnum = request.getSession().getAttributeNames();
    while (namesEnum.hasMoreElements()) {
      String name = namesEnum.nextElement();
      try {
        state.put(name, request.getSession().getAttribute(name));
      } catch (JSONException e) {
        throw new ServletException(e);
      }
    }
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    try {
      state.write(response.getWriter());
    } catch (JSONException e) {
      throw new ServletException(e);
    }
  }
}
