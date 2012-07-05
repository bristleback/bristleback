package pl.bristleback.server.bristle.engine.netty;

import org.apache.log4j.Logger;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.http.websocket.DefaultWebSocketFrame;
import org.jboss.netty.util.CharsetUtil;
import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.api.WebsocketConnector;
import pl.bristleback.server.bristle.conf.EngineConfig;
import pl.bristleback.server.bristle.engine.OperationCodes;
import pl.bristleback.server.bristle.engine.WebsocketVersions;
import pl.bristleback.server.bristle.engine.base.AbstractServerEngine;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * //@todo class description
 * <p/>
 * Created on:2011-07-11 11:13:21<br/>
 *
 * @author Wojciech Niemiec
 */
@Component("system.engine.netty")
public class NettyServerEngine extends AbstractServerEngine {
  private static Logger log = Logger.getLogger(NettyServerEngine.class.getName());

  private Channel serverChannel;

  @Override
  public void startServer() {
    log.info("Starting Netty engine (" + getClass().getName() + ") on port " + getEngineConfiguration().getPort());
    EngineConfig engineConfig = getEngineConfiguration();
    ChannelFactory nioChannelFactory =
      new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool());
    ServerBootstrap bootstrap = new ServerBootstrap(nioChannelFactory);
    bootstrap.setPipelineFactory(new WebsocketChannelPipelineFactory(engineConfig, this));
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
  public void sendPacket(WebsocketConnector connector, String contentAsString) {
    Channel channel = ((NettyConnector) connector).getChannel();
    if (connector.getWebsocketVersion().equals(WebsocketVersions.HYBI_13.getVersionCode())) {
      channel.write(new DefaultWebSocketFrame(OperationCodes.TEXT_FRAME_CODE.getCode(),
        ChannelBuffers.copiedBuffer(contentAsString, CharsetUtil.UTF_8)));
    } else {
      channel.write(new DefaultWebSocketFrame(contentAsString));
    }
  }

  @Override
  public void sendPacket(WebsocketConnector connector, byte[] contentAsBytes) {
    Channel channel = ((NettyConnector) connector).getChannel();
    channel.write(new DefaultWebSocketFrame(OperationCodes.BINARY_FRAME_CODE.getCode(),
      ChannelBuffers.copiedBuffer(contentAsBytes)));

  }
}