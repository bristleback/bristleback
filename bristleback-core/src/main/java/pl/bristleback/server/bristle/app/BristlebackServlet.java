package pl.bristleback.server.bristle.app;


import pl.bristleback.server.bristle.api.BristlebackConfig;
import pl.bristleback.server.bristle.api.ServerEngine;
import pl.bristleback.server.bristle.api.ServletServerEngine;
import pl.bristleback.server.bristle.conf.BristleInitializationException;
import pl.bristleback.server.bristle.conf.resolver.init.PojoConfigResolver;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class BristlebackServlet extends HttpServlet {

  private BristlebackServerInstance serverInstance;

  private ServletServerEngine servletServerEngine;

  @Override
  public void init() throws ServletException {
    PojoConfigResolver configResolver = new PojoConfigResolver();
    configResolver.setEngineName("system.engine.jetty.servlet");
    configResolver.setLoggingLevel("DEBUG");
    BristlebackBootstrap bristlebackBootstrap = BristlebackBootstrap.init(configResolver);

    serverInstance = bristlebackBootstrap.createServerInstance();
    servletServerEngine = getServletEngine(serverInstance.getConfiguration());

    serverInstance.startServer();
  }

  private ServletServerEngine getServletEngine(BristlebackConfig configuration) {
    ServerEngine serverEngine = configuration.getServerEngine();
    if (!(serverEngine instanceof ServletServerEngine)) {
      throw new BristleInitializationException("Cannot start Bristleback servlet WebSockets engine. "
        + "Given engine does not implement " + ServletServerEngine.class.getName() + " interface.");
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
