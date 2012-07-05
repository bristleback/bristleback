package pl.bristleback.server.bristle.exceptions;

import org.apache.log4j.Logger;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-07-03 23:51:08 <br/>
 *
 * @author Wojciech Niemiec
 */
public class BristleInitializationException extends RuntimeException {
  private static Logger log = Logger.getLogger(BristleInitializationException.class.getName());


  public BristleInitializationException(String s) {
    super(s);
  }

  public BristleInitializationException(String s, Throwable throwable) {
    super(s, throwable);
  }
}
