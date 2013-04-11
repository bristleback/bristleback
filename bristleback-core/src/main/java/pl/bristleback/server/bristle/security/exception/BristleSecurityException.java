package pl.bristleback.server.bristle.security.exception;

/**
 * Base Bristleback Security System exception, extended by more precise exceptions.
 * In almost every exception situation, objects of this class contain information about username,
 * to which this exception is addressed.
 * <p/>
 * Created on: 30.03.13 09:05 <br/>
 *
 * @author Wojciech Niemiec
 */
public class BristleSecurityException extends RuntimeException {

  private String username;

  public BristleSecurityException() {
  }

  public BristleSecurityException(String username) {
    this.username = username;
  }

  public BristleSecurityException(String username, String message) {
    super(message);
    this.username = username;
  }

  public BristleSecurityException(String username, String message, Throwable cause) {
    super(message, cause);
    this.username = username;
  }

  public String getUsername() {
    return username;
  }
}
