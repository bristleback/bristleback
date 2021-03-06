/*
 * Bristleback Websocket Framework - Copyright (c) 2010-2013 http://bristleback.pl
 * ---------------------------------------------------------------------------
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation; either version 3 of the License, or (at your
 * option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but without any warranty; without even the implied warranty of merchantability
 * or fitness for a particular purpose.
 * You should have received a copy of the GNU Lesser General Public License along
 * with this program; if not, see <http://www.gnu.org/licenses/lgpl.html>.
 * ---------------------------------------------------------------------------
 */

package pl.bristleback.server.bristle.engine.base;

import org.apache.log4j.Logger;
import pl.bristleback.server.bristle.api.BristlebackConfig;
import pl.bristleback.server.bristle.api.ConnectionStateListener;
import pl.bristleback.server.bristle.api.ServerEngine;
import pl.bristleback.server.bristle.api.WebsocketConnector;
import pl.bristleback.server.bristle.api.users.UserContext;
import pl.bristleback.server.bristle.conf.EngineConfig;
import pl.bristleback.server.bristle.listener.ConnectionStateListenerChain;
import pl.bristleback.server.bristle.security.UsersContainer;

import javax.inject.Inject;
import java.util.List;

/**
 * This class is intended to be extended by more specific Websocket Engine implementations.
 * It provides an access to Websocket Engine configuration as well as Bristleback configuration.
 * It also fully implements connection open/close events, which are sent by the connectors.
 * <p/>
 * Created on: 2011-07-09 16:13:41 <br/>
 *
 * @author Wojciech Niemiec
 */
public abstract class AbstractServerEngine implements ServerEngine {

  private static Logger log = Logger.getLogger(AbstractServerEngine.class.getName());

  private EngineConfig engineConfig;

  private BristlebackConfig configuration;

  @Inject
  private UsersContainer usersContainer;

  public void init(BristlebackConfig bristlebackConfiguration) {
    this.configuration = bristlebackConfiguration;
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
    UserContext user = usersContainer.newUser(connector);

    List<ConnectionStateListener> listeners = configuration.getListenersContainer().getConnectionStateListeners();
    ConnectionStateListenerChain chain = new ConnectionStateListenerChain(listeners);
    chain.connectorStarted(user);
    log.info("New " + connector.getClass().getSimpleName() + " connector started, connector id=" + connector.getConnectorId());
  }

  @Override
  public void onConnectionClose(WebsocketConnector connector) {
    UserContext user = usersContainer.getUserContext(connector);
    usersContainer.removeUser(connector);

    List<ConnectionStateListener> listeners = configuration.getListenersContainer().getConnectionStateListeners();
    ConnectionStateListenerChain chain = new ConnectionStateListenerChain(listeners);
    chain.connectorStopped(user);

    log.info("Connector has stopped - id: " + connector.getConnectorId());
  }

}
