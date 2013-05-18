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

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.http.HttpChunkAggregator;
import org.jboss.netty.handler.codec.http.HttpRequestDecoder;
import org.jboss.netty.handler.codec.http.HttpResponseEncoder;
import org.jboss.netty.util.HashedWheelTimer;
import org.jboss.netty.util.Timer;
import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.api.ServerEngine;
import pl.bristleback.server.bristle.conf.EngineConfig;

import javax.inject.Inject;
import java.util.concurrent.TimeUnit;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-07-11 11:38:22 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component
public class WebsocketChannelPipelineFactory implements ChannelPipelineFactory {

  @Inject
  private WebSocketServerHandler webSocketServerHandler;

  private EngineConfig engineConfig;

  private IdleStateHandler idleStateHandler;

  public void init(ServerEngine engine) {
    this.engineConfig = engine.getEngineConfiguration();
    webSocketServerHandler.init(engine);
    Timer timer = new HashedWheelTimer();
    idleStateHandler = new IdleStateHandler(timer, engineConfig.getTimeout(), TimeUnit.MILLISECONDS);
  }

  public ChannelPipeline getPipeline() throws Exception {
    ChannelPipeline pipeline = Channels.pipeline();
    pipeline.addLast("decoder", new HttpRequestDecoder());
    pipeline.addLast("aggregator", new HttpChunkAggregator(engineConfig.getMaxFrameSize()));
    pipeline.addLast("encoder", new HttpResponseEncoder());
    pipeline.addLast("handler", webSocketServerHandler);
    if (engineConfig.getTimeout() > 0) {
      pipeline.addLast("idleHandler", idleStateHandler);
    }
    return pipeline;
  }
}