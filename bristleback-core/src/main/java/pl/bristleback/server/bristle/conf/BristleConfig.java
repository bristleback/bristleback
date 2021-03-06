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

package pl.bristleback.server.bristle.conf;

import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.api.BristlebackConfig;
import pl.bristleback.server.bristle.api.DataController;
import pl.bristleback.server.bristle.api.SerializationEngine;
import pl.bristleback.server.bristle.api.ServerEngine;
import pl.bristleback.server.bristle.integration.spring.BristleSpringIntegration;
import pl.bristleback.server.bristle.listener.ListenersContainer;

/**
 * Basic implementation of Bristleback configuration interface,
 * providing simple POJO class for all necessary Bristleback components.
 * <p/>
 * Created on: 2011-07-03 23:25:51 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component
public class BristleConfig implements BristlebackConfig {

  private BristleSpringIntegration springIntegration;

  private ListenersContainer listenersContainer;

  private ServerEngine serverEngine;

  private MessageConfiguration messageConfiguration;

  private DataControllers dataControllers;

  private InitialConfiguration initialConfiguration;

  private SerializationEngine serializationEngine;

  private UserConfiguration userConfiguration;

  public ServerEngine getServerEngine() {
    return serverEngine;
  }

  public void setServerEngine(ServerEngine serverEngine) {
    this.serverEngine = serverEngine;
  }

  public DataController getDataController(String controllerName) {
    return dataControllers.getDataController(controllerName);
  }

  public DataControllers getDataControllers() {
    return dataControllers;
  }

  public void setDataControllers(DataControllers dataControllers) {
    this.dataControllers = dataControllers;
  }

  public InitialConfiguration getInitialConfiguration() {
    return initialConfiguration;
  }

  public void setInitialConfiguration(InitialConfiguration initialConfiguration) {
    this.initialConfiguration = initialConfiguration;
  }

  public ListenersContainer getListenersContainer() {
    return listenersContainer;
  }

  public void setListenersContainer(ListenersContainer listenersContainer) {
    this.listenersContainer = listenersContainer;
  }

  public SerializationEngine getSerializationEngine() {
    return serializationEngine;
  }

  public void setSerializationEngine(SerializationEngine serializationEngine) {
    this.serializationEngine = serializationEngine;
  }

  public BristleSpringIntegration getSpringIntegration() {
    return springIntegration;
  }

  public void setSpringIntegration(BristleSpringIntegration springIntegration) {
    this.springIntegration = springIntegration;
  }

  public MessageConfiguration getMessageConfiguration() {
    return messageConfiguration;
  }

  public void setMessageConfiguration(MessageConfiguration messageConfiguration) {
    this.messageConfiguration = messageConfiguration;
  }

  public UserConfiguration getUserConfiguration() {
    return userConfiguration;
  }

  public void setUserConfiguration(UserConfiguration userConfiguration) {
    this.userConfiguration = userConfiguration;
  }
}
