package pl.bristleback.server.mock;

import pl.bristleback.server.bristle.api.BristlebackConfig;
import pl.bristleback.server.bristle.api.ServerEngine;
import pl.bristleback.server.bristle.api.WebsocketConnector;
import pl.bristleback.server.bristle.conf.EngineConfig;

/**
 * //@todo class description
 * <p/>
 * Created on: 11.04.13 23:29 <br/>
 *
 * @author Wojciech Niemiec
 */
public class MockServerEngine implements ServerEngine {

  @Override
  public void startServer() {
  }

  @Override
  public void stopServer() {
  }

  @Override
  public void sendMessage(WebsocketConnector connector, String contentAsString) throws Exception {
  }

  @Override
  public void sendMessage(WebsocketConnector connector, byte[] contentAsBytes) throws Exception {
  }

  @Override
  public EngineConfig getEngineConfiguration() {
    return null;
  }

  @Override
  public BristlebackConfig getConfiguration() {
    return null;
  }

  @Override
  public void onConnectionOpened(WebsocketConnector connector) {
  }

  @Override
  public void onConnectionClose(WebsocketConnector connector) {
  }

  @Override
  public void init(BristlebackConfig configuration) {
  }
}
