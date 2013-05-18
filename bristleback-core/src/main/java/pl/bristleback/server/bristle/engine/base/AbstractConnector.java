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

import pl.bristleback.server.bristle.api.DataController;
import pl.bristleback.server.bristle.api.ServerEngine;
import pl.bristleback.server.bristle.api.WebsocketConnector;

import java.util.HashMap;
import java.util.Map;

/**
 * This class contains basic fields used by most connector implementations.
 * <p/>
 * Created on: 2011-07-18 10:00:25 <br/>
 *
 * @author Wojciech Niemiec
 */
public abstract class AbstractConnector implements WebsocketConnector {

  private String connectorId;

  private ServerEngine engine;

  private DataController dataController;

  private String websocketVersion;

  private Map<String, Object> variables;

  public AbstractConnector(ServerEngine engine, DataController controller) {
    this.engine = engine;
    connectorId = engine.getEngineConfiguration().getNextConnectorId() + "";
    dataController = controller;
    variables = new HashMap<String, Object>();
  }

  public String getConnectorId() {
    return connectorId;
  }

  @Override
  public ServerEngine getEngine() {
    return engine;
  }

  public DataController getDataController() {
    return dataController;
  }

  public String getWebsocketVersion() {
    return websocketVersion;
  }

  public void setWebsocketVersion(String websocketVersion) {
    this.websocketVersion = websocketVersion;
  }

  public Map<String, Object> getVariables() {
    return variables;
  }

  @Override
  public Object getVariable(String variableName) {
    return variables.get(variableName);
  }

  @Override
  public void putVariable(String variableName, Object variableValue) {
    variables.put(variableName, variableValue);
  }
}