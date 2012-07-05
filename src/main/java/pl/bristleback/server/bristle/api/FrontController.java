package pl.bristleback.server.bristle.api;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-11-22 17:52:26 <br/>
 *
 * @author Wojciech Niemiec
 */
public interface FrontController {

  void processCommand(WebsocketConnector connector, int operationCode, Object data);
}
