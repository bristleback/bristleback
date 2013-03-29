package pl.bristleback.server.bristle.engine.servlet;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.HttpRequestHandler;
import pl.bristleback.server.bristle.api.BristlebackConfig;
import pl.bristleback.server.bristle.api.InitialConfigurationResolver;
import pl.bristleback.server.bristle.api.ServerEngine;
import pl.bristleback.server.bristle.api.ServletServerEngine;
import pl.bristleback.server.bristle.app.BristlebackServerInstance;
import pl.bristleback.server.bristle.conf.runner.ServerInstanceResolver;
import pl.bristleback.server.bristle.conf.BristleInitializationException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-04-25 16:59:49 <br/>
 *
 * @author Wojciech Niemiec
 */
public class BristlebackHttpHandler implements HttpRequestHandler, ApplicationContextAware {

  private InitialConfigurationResolver initialConfigurationResolver;
  private ApplicationContext applicationContext;

  private BristlebackServerInstance serverInstance;

  private ServletServerEngine servletServerEngine;

  @PostConstruct
  public void startServer() {
    ServerInstanceResolver serverInstanceResolver = new ServerInstanceResolver(initialConfigurationResolver, applicationContext);
    serverInstance = serverInstanceResolver.resolverServerInstance();

    setServletEngine(serverInstance.getConfiguration());
    serverInstance.startServer();
  }

  protected void setServletEngine(BristlebackConfig configuration) {
    ServerEngine serverEngine = configuration.getServerEngine();
    if (!(serverEngine instanceof ServletServerEngine)) {
      throw new BristleInitializationException("Cannot start Bristleback servlet WebSockets engine. "
        + "Given engine does not implement " + ServletServerEngine.class.getName() + " interface.");
    }
    servletServerEngine = (ServletServerEngine) serverEngine;
  }


  @PreDestroy
  public void stopServer() {
    serverInstance.stopServer();
  }

  @Override
  public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    servletServerEngine.handleRequest(request, response);
  }

  public void setInitialConfigurationResolver(InitialConfigurationResolver initialConfigurationResolver) {
    this.initialConfigurationResolver = initialConfigurationResolver;
  }

  public void setApplicationContext(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }
}
