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


import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.websocket.WebSocketFrame;
import pl.bristleback.server.bristle.api.WebsocketConnector;

public class WebSocketServerHandler extends SimpleChannelUpstreamHandler {

  private NettyServerEngine engine;
  private HttpRequestHandler httpRequestHandler;
  private WebsocketFrameHandler websocketFrameHandler;

  public WebSocketServerHandler(NettyServerEngine engine) {
    this.engine = engine;
    httpRequestHandler = new HttpRequestHandler(engine);
    websocketFrameHandler = new WebsocketFrameHandler();
  }

  public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
    Object msg = e.getMessage();
    if (msg instanceof HttpRequest) {
      httpRequestHandler.handleHttpRequest(ctx, (HttpRequest) msg);
    } else if (msg instanceof WebSocketFrame) {
      websocketFrameHandler.handleWebSocketFrame(ctx, (WebSocketFrame) msg);
    }
  }


  public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
    super.channelDisconnected(ctx, e);
    WebsocketConnector connector = (WebsocketConnector) ctx.getAttachment();
    if (connector != null) {
      engine.onConnectionClose(connector);
    }
  }

  public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
    throws Exception {
    e.getCause().printStackTrace();
    e.getChannel().close();
  }
}