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

package pl.bristleback.server.bristle.api;

import java.util.Map;

/**
 * Websocket connector is an abstract representation of single client connection.
 * Object implementing this interface has physical implementation of network socket as well as other information,
 * like used sub protocol (in Bristleback it's one of the {@link pl.bristleback.server.bristle.api.DataController DataController} interface implementation)
 * or connection id number.
 * <p/>
 * Created on: 2011-07-14 16:39:33 <br/>
 *
 * @author Wojciech Niemiec
 */
public interface WebsocketConnector<T> {

  /**
   * Gets {@link pl.bristleback.server.bristle.api.DataController DataController} used by client represented by this connector.
   * Data controller (sub protocol, according to WebSockets specification) is chosen by client when connecting.
   *
   * @return data controller used in connection.
   */
  DataController getDataController();

  /**
   * Gets connection ID number.
   *
   * @return connection ID number.
   */
  String getConnectorId();

  /**
   * Physically disconnects client, releasing all resources used by this connection and invoking
   * {@link pl.bristleback.server.bristle.api.ServerEngine#onConnectionClose(WebsocketConnector)} method.
   */
  void stop();

  /**
   * Gets Server Engine used.
   *
   * @return server engine.
   */
  ServerEngine getEngine();

  /**
   * Gets version of WebSockets protocol used by this connection.
   *
   * @return WebSockets version.
   */
  String getWebsocketVersion();

  /**
   * Sets version of WebSockets protocol used by this connection.
   *
   * @param version WebSockets version.
   */
  void setWebsocketVersion(String version);

  Map<String, Object> getVariables();

  Object getVariable(String variableName);

  void putVariable(String variableName, Object variableValue);
}