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

package pl.bristleback.server.bristle.engine.jetty.servlet;

import org.apache.log4j.Logger;
import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketFactory;
import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.api.DataController;
import pl.bristleback.server.bristle.api.FrontController;
import pl.bristleback.server.bristle.api.ServletServerEngine;
import pl.bristleback.server.bristle.api.WebsocketConnector;
import pl.bristleback.server.bristle.engine.base.AbstractServerEngine;
import pl.bristleback.server.bristle.engine.jetty.JettyConnector;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-04-25 17:02:01 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component(JettyServletWebsocketEngine.ENGINE_NAME)
public class JettyServletWebsocketEngine extends AbstractServerEngine implements WebSocketFactory.Acceptor, ServletServerEngine {
  private static Logger log = Logger.getLogger(JettyServletWebsocketEngine.class.getName());

  public static final String ENGINE_NAME = "system.engine.jetty.servlet";

  private WebSocketFactory websocketFactory;

  @Inject
  @Named("defaultFrontController")
  private FrontController frontController;

  private void init() {
    setWebsocketFactoryParameters();
  }

  private void setWebsocketFactoryParameters() {
    websocketFactory = new WebSocketFactory(this, getEngineConfiguration().getMaxBufferSize());
    websocketFactory.setMaxBinaryMessageSize(getEngineConfiguration().getMaxFrameSize());
    websocketFactory.setMaxTextMessageSize(getEngineConfiguration().getMaxFrameSize());
    websocketFactory.setMaxIdleTime(getEngineConfiguration().getTimeout());
  }

  public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    if (!websocketFactory.acceptWebSocket(request, response)) {
      throw new ServletException("Request is not a valid WebSockets handshake.");
    }
  }

  @Override
  public void startServer() {
    init();
    log.info("Bristleback Jetty servlet engine started.");
  }

  @Override
  public void stopServer() {
    log.info("Bristleback Jetty servlet engine stopped.");
  }

  @Override
  public void sendMessage(WebsocketConnector connector, String contentAsString) throws Exception {
    ((JettyConnector) connector).getConnection().sendMessage(contentAsString);
  }

  @Override
  public void sendMessage(WebsocketConnector connector, byte[] contentAsBytes) throws Exception {
    ((JettyConnector) connector).getConnection().sendMessage(contentAsBytes, 0, contentAsBytes.length);
  }

  @Override
  public WebSocket doWebSocketConnect(HttpServletRequest request, String protocol) {
    DataController controller = getConfiguration().getDataController(protocol);
    return new JettyConnector(this, controller, frontController);
  }

  @Override
  public boolean checkOrigin(HttpServletRequest request, String origin) {
    return true;
  }
}
