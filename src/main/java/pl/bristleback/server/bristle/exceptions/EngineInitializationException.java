package pl.bristleback.server.bristle.exceptions;

import org.apache.log4j.Logger;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-07-09 15:00:20 <br/>
 *
 * @author Wojciech Niemiec
 */
public class EngineInitializationException extends RuntimeException {
  private static Logger log = Logger.getLogger(EngineInitializationException.class.getName());

  public EngineInitializationException() {
  }

  public EngineInitializationException(String s) {
    super(s);
  }

  public EngineInitializationException(String s, Throwable throwable) {
    super(s, throwable);
  }
}
