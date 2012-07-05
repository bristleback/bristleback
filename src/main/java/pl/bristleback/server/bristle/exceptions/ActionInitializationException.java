package pl.bristleback.server.bristle.exceptions;

import org.apache.log4j.Logger;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-07-23 13:08:14 <br/>
 *
 * @author Wojciech Niemiec
 */
public class ActionInitializationException extends RuntimeException {
  private static Logger log = Logger.getLogger(ActionInitializationException.class.getName());

  public ActionInitializationException() {
  }

  public ActionInitializationException(String message) {
    super(message);
  }

  public ActionInitializationException(String message, Throwable cause) {
    super(message, cause);
  }
}