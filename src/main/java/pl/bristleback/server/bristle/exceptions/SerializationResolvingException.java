package pl.bristleback.server.bristle.exceptions;

import org.apache.log4j.Logger;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-10-09 15:31:22 <br/>
 *
 * @author Wojciech Niemiec
 */
public class SerializationResolvingException extends BristleRuntimeException {
  private static Logger log = Logger.getLogger(SerializationResolvingException.class.getName());

  public SerializationResolvingException(String message) {
    super(message);
  }
}