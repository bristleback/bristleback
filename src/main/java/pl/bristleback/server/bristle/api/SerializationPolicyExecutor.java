package pl.bristleback.server.bristle.api;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-09-18 15:33:44 <br/>
 *
 * @author Wojciech Niemiec
 */
public interface SerializationPolicyExecutor {

  void sendUsingPolicy(WebsocketMessage message, ServerEngine engine) throws Exception;
} 
