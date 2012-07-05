package pl.bristleback.server.mock;

import pl.bristleback.server.bristle.api.ServerEngine;
import pl.bristleback.server.bristle.engine.netty.NettyServerEngine;

/**
 * Pawel Machowski
 * created at 11.02.12 15:29
 */
public class MockFactory {

  public static ServerEngine getMockServerEngine() {
    NettyServerEngine nettyServerEngine = new NettyServerEngine();
    nettyServerEngine.setConfiguration(ConfigurationMockFactory.getConfiguration(null));
    return nettyServerEngine;
  }

}
