package pl.bristleback.server.bristle.engine.netty;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.timeout.IdleState;
import org.jboss.netty.util.Timer;

import java.util.concurrent.TimeUnit;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-07-18 12:39:14 <br/>
 *
 * @author Wojciech Niemiec
 */
public class IdleStateHandler extends org.jboss.netty.handler.timeout.IdleStateHandler {

  public IdleStateHandler(Timer timer, int readerIdleTimeSeconds) {
    super(timer, readerIdleTimeSeconds, 0, 0);
  }

  public IdleStateHandler(Timer timer, long readerIdleTime, TimeUnit unit) {
    super(timer, readerIdleTime, 0, 0, unit);
  }

  @Override
  protected void channelIdle(ChannelHandlerContext ctx, IdleState state, long lastActivityTimeMillis) throws Exception {
    ctx.getChannel().disconnect();
  }
}