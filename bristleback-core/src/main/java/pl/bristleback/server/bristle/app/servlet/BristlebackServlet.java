package pl.bristleback.server.bristle.app.servlet;


import pl.bristleback.server.bristle.api.BristlebackConfig;
import pl.bristleback.server.bristle.api.ServerEngine;
import pl.bristleback.server.bristle.api.ServletServerEngine;
import pl.bristleback.server.bristle.app.BristlebackServerInstance;
import pl.bristleback.server.bristle.conf.BristleInitializationException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Bristleback servlet creates an instance of {@link BristlebackServerInstance}
 * using configuration resolving class passed as servlet init param <code><strong>resolverClass</strong></code>
 * that implements {@link pl.bristleback.server.bristle.api.ServletServerInstanceResolver} interface.
 * For example:
 * <p>
 * &lt;servlet>  <br>
 * &nbsp;&nbsp;&lt;servlet-name>bristlebackServlet&lt;/servlet-name>  <br>
 * &nbsp;&nbsp;&lt;servlet-class>pl.bristleback.server.bristle.app.servlet.BristlebackServlet&lt;/servlet-class>  <br>
 * &nbsp;&nbsp;&lt;load-on-startup>1&lt;/load-on-startup>        <br>
 * &nbsp;&nbsp;&lt;init-param>        <br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;param-name>resolverClass&lt;/param-name>       <br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;param-value>sample.ServerInstanceResolver&lt;/param-value>   <br>
 * &nbsp;&nbsp;&lt;/init-param>     <br>
 * &lt;/servlet>                   <br>
 * </p>
 * Websocket Server engine defined in {@link pl.bristleback.server.bristle.api.ServletServerInstanceResolver}
 * must implement {@link ServletServerEngine} interface to be able to handle HTTP requests.
 * Bristleback servlet extends standard Java HTTP Servlet
 *
 * @see <a href="https://github.com/bristleback/bristleback/wiki/Configuration-deeper-look#wiki-Built_in_Server_Engines">More about configuration engines</a>
 * @since 0.4.0
 */
public class BristlebackServlet extends HttpServlet {

  private BristlebackServletManager servletManager = new BristlebackServletManager();

  private BristlebackServerInstance serverInstance;

  private ServletServerEngine servletServerEngine;

  @Override
  public void init() throws ServletException {
    serverInstance = servletManager.createInitialConfigurationResolver(getServletConfig());
    servletServerEngine = getServletEngine(serverInstance.getConfiguration());

    serverInstance.startServer();
  }

  private ServletServerEngine getServletEngine(BristlebackConfig configuration) {
    ServerEngine serverEngine = configuration.getServerEngine();
    if (!(serverEngine instanceof ServletServerEngine)) {
      throw new BristleInitializationException("Cannot start Bristleback servlet WebSockets engine. "
        + "Websocket engine with name: " + serverEngine.getEngineConfiguration().getName() + " does not implement "
        + ServletServerEngine.class.getName() + " interface.");
    }
    return (ServletServerEngine) serverEngine;
  }

  @Override
  public void destroy() {
    serverInstance.stopServer();
  }

  @Override
  protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    servletServerEngine.handleRequest(request, response);
  }
}
