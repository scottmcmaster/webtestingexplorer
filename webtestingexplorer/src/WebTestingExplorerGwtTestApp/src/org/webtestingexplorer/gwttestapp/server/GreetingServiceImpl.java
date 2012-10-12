package org.webtestingexplorer.gwttestapp.server;

import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import org.webtestingexplorer.gwttestapp.client.GreetingService;
import org.webtestingexplorer.gwttestapp.shared.FieldVerifier;
import org.webtestingexplorer.server.oraclesupport.CollectingLoggingHandler;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements
    GreetingService {

	private static final Logger logger = Logger.getLogger("org.webtestingexplorer.gwttestapp.server.GreetingServiceImpl");
	
	@Override
	public void init() throws ServletException {
		super.init();
		logger.addHandler(CollectingLoggingHandler.getInstance());
	}
	
  public String greetServer(String input) throws IllegalArgumentException {
    // Verify that the input is valid. 
    if (!FieldVerifier.isValidName(input)) {
      // If the input is not valid, throw an IllegalArgumentException back to
      // the client.
      throw new IllegalArgumentException(
          "Name must be at least 4 characters long");
    }

    String serverInfo = getServletContext().getServerInfo();
    String userAgent = getThreadLocalRequest().getHeader("User-Agent");

    // For trying webtestingexplorer against a stateful session servlet.
    HttpSession session = getThreadLocalRequest().getSession();
    session.setAttribute("name", input);

    // Escape data from the client to avoid cross-site script vulnerabilities.
    input = escapeHtml(input);
    userAgent = escapeHtml(userAgent);
    
  	logger.info("All done");

    return "Hello, " + input + "!<br><br>I am running " + serverInfo
        + ".<br><br>It looks like you are using:<br>" + userAgent;
  }

  /**
   * Escape an html string. Escaping data received from the client helps to
   * prevent cross-site script vulnerabilities.
   * 
   * @param html the html string to escape
   * @return the escaped string
   */
  private String escapeHtml(String html) {
    if (html == null) {
      return null;
    }
    return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;");
  }
}
