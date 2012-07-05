package pl.bristleback.server.bristle.api;

import pl.bristleback.server.bristle.conf.EngineConfig;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-07-05 14:51:41 <br/>
 *
 * @author Wojciech Niemiec
 */
public interface ServerEngine extends ConfigurationAware {

  EngineConfig getEngineConfiguration();

  BristlebackConfig getConfiguration();

  void startServer();

  void stopServer();

  void onConnectionOpened(WebsocketConnector connector);

  void onConnectionClose(WebsocketConnector connector);

  void sendPacket(WebsocketConnector connector, String contentAsString) throws Exception;

  void sendPacket(WebsocketConnector connector, byte[] contentAsBytes) throws Exception;
}