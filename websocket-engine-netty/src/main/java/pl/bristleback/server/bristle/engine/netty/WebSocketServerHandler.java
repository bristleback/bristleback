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
package pl.bristleback.server.bristle.engine.netty;


import org.apache.log4j.Logger;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.websocketx.WebSocketFrame;
import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.api.ServerEngine;
import pl.bristleback.server.bristle.api.WebsocketConnector;

import javax.inject.Inject;

@Component
public class WebSocketServerHandler extends SimpleChannelUpstreamHandler {

  private static Logger log = Logger.getLogger(WebSocketServerHandler.class.getName());

  private ServerEngine engine;

  @Inject
  private HttpRequestHandler httpRequestHandler;

  @Inject
  private WebsocketFrameHandler websocketFrameHandler;

  public void init(ServerEngine serverEngine) {
    this.engine = serverEngine;
    httpRequestHandler.init(serverEngine);
  }

  public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
    Object msg = e.getMessage();
    if (msg instanceof HttpRequest) {
      httpRequestHandler.handleHttpRequest(ctx, (HttpRequest) msg);
    } else if (msg instanceof WebSocketFrame) {
      websocketFrameHandler.handleWebSocketFrame(ctx, (WebSocketFrame) msg);
    }
  }

  @SuppressWarnings("rawtypes")
  public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
    super.channelDisconnected(ctx, e);
    WebsocketConnector connector = (WebsocketConnector) ctx.getAttachment();
    if (connector != null) {
      engine.onConnectionClose(connector);
    }
  }

  public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
    throws Exception {
    log.debug(e);
    e.getChannel().close();
  }
}