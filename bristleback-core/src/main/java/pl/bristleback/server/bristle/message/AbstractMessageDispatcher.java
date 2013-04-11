package pl.bristleback.server.bristle.message;

import pl.bristleback.server.bristle.api.BristlebackConfig;
import pl.bristleback.server.bristle.api.MessageDispatcher;
import pl.bristleback.server.bristle.api.ServerEngine;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-08-23 13:23:44 <br/>
 *
 * @author Wojciech Niemiec
 */
public abstract class AbstractMessageDispatcher implements MessageDispatcher {

  private ServerEngine server;

  protected ServerEngine getServer() {
    return server;
  }

  public void setServer(ServerEngine server) {
    this.server = server;
  }

  public BristlebackConfig getConfiguration() {
    return server.getConfiguration();
  }
} 