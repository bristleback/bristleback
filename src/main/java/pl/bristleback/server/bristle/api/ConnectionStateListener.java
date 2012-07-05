package pl.bristleback.server.bristle.api;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-11-20 14:47:18 <br/>
 *
 * @author Wojciech Niemiec
 */
public interface ConnectionStateListener {

  void init(BristlebackConfig configuration);

  void connectorStarted(WebsocketConnector connector);

  void connectorStopped(WebsocketConnector connector);
}
