package pl.bristleback.server.bristle.exceptions;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-07-23 13:08:14 <br/>
 *
 * @author Wojciech Niemiec
 */
public class ActionInitializationException extends RuntimeException {

  public ActionInitializationException(String message) {
    super(message);
  }

  public ActionInitializationException(String message, Throwable cause) {
    super(message, cause);
  }
}