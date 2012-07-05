package pl.bristleback.server.bristle.serialization.system;

import org.apache.log4j.Logger;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-01-15 10:01:37 <br/>
 *
 * @author Wojciech Niemiec
 */
public class SerializationException extends RuntimeException {
  private static Logger log = Logger.getLogger(SerializationException.class.getName());

  private Reason reason;
  private PropertySerialization information;

  public SerializationException(Reason reason, PropertySerialization information) {
    this.reason = reason;
    this.information = information;
  }

  public Reason getReason() {
    return reason;
  }

  public PropertySerialization getInformation() {
    return information;
  }

  public static enum Reason {
    TYPE_MISMATCH,
    NOT_NULL_VIOLATION

  }
}
