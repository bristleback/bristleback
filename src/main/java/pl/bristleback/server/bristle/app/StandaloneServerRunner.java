package pl.bristleback.server.bristle.app;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import pl.bristleback.server.bristle.api.InitialConfigurationResolver;
import pl.bristleback.server.bristle.conf.runner.ServerInstanceResolver;
import pl.bristleback.server.bristle.exceptions.BristleInitializationException;
import pl.bristleback.server.bristle.exceptions.BristleRuntimeException;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-09-26 22:00:49 <br/>
 *
 * @author Wojciech Niemiec
 */
public final class StandaloneServerRunner implements ApplicationContextAware {
  private static Logger log = Logger.getLogger(StandaloneServerRunner.class.getName());

  private InitialConfigurationResolver initialConfigurationResolver;

  private BristlebackServerInstance serverInstance;

  private ApplicationContext actualApplicationContext;

  public void startServer() {
    if (initialConfigurationResolver == null) {
      throw new BristleInitializationException("Cannot start Bristleback Server, missing initialConfiguration resolver");
    } else if (serverInstance != null) {
      throw new BristleInitializationException("Cannot start Bristleback Server, create a new ServerRunner instance to start new server.");
    }

    ServerInstanceResolver serverInstanceResolver = new ServerInstanceResolver(initialConfigurationResolver, actualApplicationContext);
    serverInstance = serverInstanceResolver.resolverServerInstance();

    serverInstance.startServer();
  }

  public void stopServer() {
    if (serverInstance == null || !serverInstance.isRunning()) {
      throw new BristleRuntimeException("Cannot stop Bristleback server, server is not up.");
    }
    serverInstance.stopServer();
  }


  public BristlebackServerInstance getServerInstance() {
    return serverInstance;
  }

  public void setInitialConfigurationResolver(InitialConfigurationResolver initialConfigurationResolver) {
    this.initialConfigurationResolver = initialConfigurationResolver;
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) {
    this.actualApplicationContext = applicationContext;
  }
}