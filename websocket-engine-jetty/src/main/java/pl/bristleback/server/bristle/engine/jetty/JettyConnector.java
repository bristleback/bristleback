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

package pl.bristleback.server.bristle.engine.jetty;

import org.eclipse.jetty.websocket.WebSocket;
import pl.bristleback.server.bristle.api.DataController;
import pl.bristleback.server.bristle.api.FrontController;
import pl.bristleback.server.bristle.api.ServerEngine;
import pl.bristleback.server.bristle.conf.EngineConfig;
import pl.bristleback.server.bristle.engine.OperationCode;
import pl.bristleback.server.bristle.engine.base.AbstractConnector;

public class JettyConnector extends AbstractConnector implements WebSocket, WebSocket.OnTextMessage, WebSocket.OnBinaryMessage, WebSocket.OnControl {

  private Connection connection;
  private FrontController frontController;

  public JettyConnector(ServerEngine engine, DataController controller, FrontController frontController) {
    super(engine, controller);
    this.frontController = frontController;
  }

  public void onOpen(Connection newConnection) {
    EngineConfig engineConfiguration = getEngine().getEngineConfiguration();
    newConnection.setMaxBinaryMessageSize(engineConfiguration.getMaxFrameSize());
    newConnection.setMaxTextMessageSize(engineConfiguration.getMaxFrameSize());
    newConnection.setMaxIdleTime(engineConfiguration.getTimeout());
    getEngine().onConnectionOpened(this);
    this.connection = newConnection;
  }

  public void onClose(int i, String s) {
    getEngine().onConnectionClose(this);
  }

  public void stop() {
    connection.close();
  }

  public Connection getConnection() {
    return connection;
  }

  public void onMessage(String data) {
    frontController.processCommand(this, OperationCode.TEXT_FRAME_CODE.getCode(), data);
  }

  @Override
  public void onMessage(byte[] data, int offset, int length) {
    frontController.processCommand(this, OperationCode.BINARY_FRAME_CODE.getCode(), data);
  }

  @Override
  public boolean onControl(byte controlCode, byte[] data, int offset, int length) {
    frontController.processCommand(this, controlCode, data);
    return true;
  }
}
