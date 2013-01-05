package pl.bristleback.server.bristle.serialization.system;

/**
 * Exception thrown during fast deserialization process performed by {@link pl.bristleback.server.bristle.serialization.system.json.JsonSerializationEngine}.
 * It does not show the exact place in which the problem occurs, the reason for the exception is written as exception message.
 * <p/>
 * Created on: 05.01.13 13:26 <br/>
 *
 * @author Wojciech Niemiec
 */
public class DeserializationException extends RuntimeException {

  public DeserializationException(String message) {
    super(message);
  }
}
