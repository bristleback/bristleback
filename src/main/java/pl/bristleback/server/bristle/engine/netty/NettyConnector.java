package pl.bristleback.server.bristle.engine.netty;

import org.jboss.netty.channel.Channel;
import pl.bristleback.server.bristle.api.DataController;
import pl.bristleback.server.bristle.api.ServerEngine;
import pl.bristleback.server.bristle.engine.base.AbstractConnector;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-07-15 16:42:57 <br/>
 *
 * @author Wojciech Niemiec
 */
public class NettyConnector extends AbstractConnector {

  private Channel channel;

  public NettyConnector(Channel channel, ServerEngine engine, DataController controller) {
    super(engine, controller);
    this.channel = channel;
  }

  @Override
  public void stop() {
    channel.disconnect();
  }

  public Channel getChannel() {
    return channel;
  }
}