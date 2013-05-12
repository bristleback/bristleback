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

import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketHandler;
import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.api.DataController;
import pl.bristleback.server.bristle.api.FrontController;
import pl.bristleback.server.bristle.api.WebsocketConnector;
import pl.bristleback.server.bristle.engine.base.AbstractServerEngine;
import pl.bristleback.server.bristle.conf.BristleInitializationException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

@Component("system.engine.jetty")
public class JettyWebsocketEngine extends AbstractServerEngine {

  private static Logger log = Logger.getLogger(JettyWebsocketEngine.class.getName());

  @Inject
  @Named("defaultFrontController")
  private FrontController frontController;

  private Server server;

  public void startServer() {
    try {
      int port = getEngineConfiguration().getPort();
      server = new Server(port);
      final JettyWebsocketEngine thisEngine = this;
      server.setHandler(new WebSocketHandler() {
        public WebSocket doWebSocketConnect(HttpServletRequest httpServletRequest, String protocolName) {
          DataController controller = getConfiguration().getDataController(protocolName);
          return new JettyConnector(thisEngine, controller, frontController);
        }
      });
      server.start();
    } catch (Exception e) {
      throw new BristleInitializationException("Jetty engine could not start.", e);
    }
  }

  public void stopServer() {
    try {
      server.stop();
    } catch (Exception e) {
      log.error("Exception while trying to stop Jetty server");
    }
  }

  public void sendMessage(WebsocketConnector connector, String contentAsString) throws Exception {
    ((JettyConnector) connector).getConnection().sendMessage(contentAsString);
  }

  public void sendMessage(WebsocketConnector connector, byte[] contentAsBytes) throws Exception {
    ((JettyConnector) connector).getConnection().sendMessage(contentAsBytes, 0, contentAsBytes.length);
  }
}
