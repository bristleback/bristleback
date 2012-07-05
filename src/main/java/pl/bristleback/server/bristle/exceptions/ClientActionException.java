package pl.bristleback.server.bristle.exceptions;

import org.apache.log4j.Logger;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-05-29 22:28:28 <br/>
 *
 * @author Wojciech Niemiec
 */
public class ClientActionException extends BristleRuntimeException {
  private static Logger log = Logger.getLogger(ClientActionException.class.getName());

  public ClientActionException(String message) {
    super(message);
  }
}
