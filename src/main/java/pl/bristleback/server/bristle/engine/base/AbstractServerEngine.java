package pl.bristleback.server.bristle.engine.base;

import org.apache.log4j.Logger;
import pl.bristleback.server.bristle.api.BristlebackConfig;
import pl.bristleback.server.bristle.api.ConnectionStateListener;
import pl.bristleback.server.bristle.api.ServerEngine;
import pl.bristleback.server.bristle.api.WebsocketConnector;
import pl.bristleback.server.bristle.conf.EngineConfig;
import pl.bristleback.server.bristle.listener.ConnectionStateListenerChain;

import java.util.List;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-07-09 16:13:41 <br/>
 *
 * @author Wojciech Niemiec
 */
public abstract class AbstractServerEngine implements ServerEngine {
  private static Logger log = Logger.getLogger(AbstractServerEngine.class.getName());

  private EngineConfig engineConfig;
  private BristlebackConfig configuration;

  public void setConfiguration(BristlebackConfig configuration) {
    this.configuration = configuration;
    this.engineConfig = configuration.getInitialConfiguration().getEngineConfiguration();
  }

  public BristlebackConfig getConfiguration() {
    return configuration;
  }

  public EngineConfig getEngineConfiguration() {
    return engineConfig;
  }

  @Override
  public void onConnectionOpened(WebsocketConnector connector) {
    List<ConnectionStateListener> listeners = configuration.getListenersContainer().getConnectionStateListeners();
    ConnectionStateListenerChain chain = new ConnectionStateListenerChain(listeners);
    chain.connectorStarted(connector);
    log.info("New " + connector.getClass().getSimpleName() + " connector started.");
  }

  @Override
  public void onConnectionClose(WebsocketConnector connector) {
    List<ConnectionStateListener> listeners = configuration.getListenersContainer().getConnectionStateListeners();
    //framework handlers will always run first
    ConnectionStateListenerChain chain = new ConnectionStateListenerChain(listeners);
    chain.connectorStopped(connector);
    log.info(connector.getClass().getSimpleName() + " connector stopped.");
  }

}
