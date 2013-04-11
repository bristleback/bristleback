package pl.bristleback.server.bristle.api;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This is an extension for {@link pl.bristleback.server.bristle.api.ServerEngine ServerEngine} interface.
 * Provides functionality for accepting incoming HTTP requests as WebSockets connection.
 * Servlet server engines are required in web applications. Servlet engines works in combination with
 * {@link pl.bristleback.server.bristle.engine.servlet.BristlebackHttpHandler} and Spring Framework DispatcherServlet,
 * <p/>
 * Created on: 2012-04-25 17:05:35 <br/>
 *
 * @author Wojciech Niemiec
 * @see pl.bristleback.server.bristle.engine.servlet.BristlebackHttpHandler
 * @see <a href="http://static.springsource.org/spring/docs/3.0.x/javadoc-api/org/springframework/web/servlet/DispatcherServlet.html">Spring DispatcherServlet JavaDoc</a>
 */
public interface ServletServerEngine extends ServerEngine {

  /**
   * Handles incoming request and switch protocol used into WebSockets.
   *
   * @param request  HTTP request
   * @param response HTTP response
   * @throws ServletException in case of general errors
   * @throws IOException      in case of I/O errors
   */
  void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
}
