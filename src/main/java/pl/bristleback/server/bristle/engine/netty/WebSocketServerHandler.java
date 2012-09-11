/*
 * Copyright 2010 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
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