package pl.bristleback.server.bristle.action.exception;

import pl.bristleback.server.bristle.BristleRuntimeException;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-05-29 22:28:28 <br/>
 *
 * @author Wojciech Niemiec
 */
public class ClientActionException extends BristleRuntimeException {

  public ClientActionException(String message) {
    super(message);
  }
}
