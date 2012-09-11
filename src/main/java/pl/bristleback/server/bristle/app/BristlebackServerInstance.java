package pl.bristleback.server.bristle.app;

import pl.bristleback.server.bristle.api.BristlebackConfig;
import pl.bristleback.server.bristle.api.MessageDispatcher;
import pl.bristleback.server.bristle.api.ServerEngine;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-01-22 22:00:29 <br/>
 *
 * @author Wojciech Niemiec
 */
public class BristlebackServerInstance {

  private boolean running;

  private ServerEngine engine;
  private MessageDispatcher messageDispatcher;
  private BristlebackConfig configuration;

  public BristlebackServerInstance(BristlebackConfig configuration) {
    this.configuration = configuration;
    engine = configuration.getServerEngine();
    messageDispatcher = configuration.getMessageConfiguration().getMessageDispatcher();
  }

  public void startServer() {
    running = true;
    engine.startServer();
    messageDispatcher.startDispatching();
  }

  public void stopServer() {
    running = false;
    messageDispatcher.stopDispatching();
    engine.stopServer();
  }

  public boolean isRunning() {
    return running;
  }

  public BristlebackConfig getConfiguration() {
    return configuration;
  }
}

