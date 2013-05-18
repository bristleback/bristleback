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

package pl.bristleback.server.bristle.engine.tomcat.servlet;

import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;
import pl.bristleback.server.bristle.api.DataController;
import pl.bristleback.server.bristle.engine.tomcat.TomcatConnector;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class TomcatWebsocketServletHolder extends WebSocketServlet {

  private TomcatServletWebsocketEngine engine;

  public TomcatWebsocketServletHolder(TomcatServletWebsocketEngine engine) {
    this.engine = engine;
  }

  @Override
  protected String selectSubProtocol(List<String> subProtocols) {
    return subProtocols.get(0);
  }

  @Override
  protected StreamInbound createWebSocketInbound(String protocol, HttpServletRequest servletRequest) {
    DataController controller = engine.getConfiguration().getDataController(protocol);
    return new TomcatConnector(engine, controller, engine.getFrontController());
  }
}
