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
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import org.jboss.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.jboss.netty.util.CharsetUtil;
import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.api.WebsocketConnector;
import pl.bristleback.server.bristle.conf.EngineConfig;
import pl.bristleback.server.bristle.engine.WebsocketVersions;
import pl.bristleback.server.bristle.engine.base.AbstractServerEngine;

import javax.inject.Inject;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * //@todo class description
 * <p/>
 * Created on:2011-07-11 11:13:21<br/>
 *
 * @author Wojciech Niemiec
 * @author Andrea Nanni
 */
@Component("system.engine.netty")
public class NettyServerEngine extends AbstractServerEngine {
  private static Logger log = Logger.getLogger(NettyServerEngine.class.getName());

  private Channel serverChannel;

  @Inject
  private WebsocketChannelPipelineFactory pipelineFactory;

  @Override
  public void startServer() {
    log.info("Starting Netty engine (" + getClass().getName() + ") on port " + getEngineConfiguration().getPort());
    EngineConfig engineConfig = getEngineConfiguration();
    ChannelFactory nioChannelFactory =
      new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool());
    ServerBootstrap bootstrap = new ServerBootstrap(nioChannelFactory);
    pipelineFactory.init(this);
    bootstrap.setPipelineFactory(pipelineFactory);
    serverChannel = bootstrap.bind(new InetSocketAddress(engineConfig.getPort()));
    log.info("Netty engine (" + getClass().getName() + ") on port " + getEngineConfiguration().getPort() + " has started.");
  }

  @Override
  public void stopServer() {
    log.info("Stopping Netty engine (" + getClass().getName() + ") on port " + getEngineConfiguration().getPort());
    if (serverChannel != null) {
      serverChannel.close();
      serverChannel.getFactory().releaseExternalResources();
    }
    log.info("Netty engine (" + getClass().getName() + ") on port " + getEngineConfiguration().getPort() + " has stopped.");
  }

  @Override
  public void sendMessage(WebsocketConnector connector, String contentAsString) {
    Channel channel = ((NettyConnector) connector).getChannel();
    if (connector.getWebsocketVersion().equals(WebsocketVersions.HYBI_13.getVersionCode())) {
      channel.write(new TextWebSocketFrame(ChannelBuffers.copiedBuffer(contentAsString, CharsetUtil.UTF_8)));
    } else {
      channel.write(new TextWebSocketFrame(contentAsString));
    }
  }

  @Override
  public void sendMessage(WebsocketConnector connector, byte[] contentAsBytes) {
    Channel channel = ((NettyConnector) connector).getChannel();
    channel.write(new BinaryWebSocketFrame(ChannelBuffers.copiedBuffer(contentAsBytes)));

  }
}
