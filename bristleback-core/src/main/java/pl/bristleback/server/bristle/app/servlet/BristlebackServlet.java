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
