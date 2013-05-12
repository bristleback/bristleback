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

/**
 * Implementations of this interface should be able to handle every control and non control message,
 * coming from low level Websocket connector implementation.
 * <p/>
 * Created on: 2011-11-22 17:52:26 <br/>
 *
 * @author Wojciech Niemiec
 * @see pl.bristleback.server.bristle.engine.controller.DefaultFrontController DefaultFrontController
 */
public interface FrontController {

  /**
   * Processes incoming command, with unspecified type of data and operation code given as integer number.
   *
   * @param connector     low level, server engine dependent abstraction of client-server connection.
   * @param operationCode code of requested operation. List of available operation code is specified by WebSockets protocol
   *                      and also can be found in {@link pl.bristleback.server.bristle.engine.OperationCode OperationCode} enumeration.
   * @param data          data sent by client.
   * @see pl.bristleback.server.bristle.engine.OperationCode Operation code
   */
  void processCommand(WebsocketConnector connector, int operationCode, Object data);
}
