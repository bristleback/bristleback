package pl.bristleback.server.bristle.security.exception;

/**
 * Exception thrown when user with given name cannot be found or password for that user is not valid.
 * <p/>
 * Created on: 19.02.13 22:47 <br/>
 *
 * @author Wojciech Niemiec
 */
public class IncorrectUsernameOrPasswordException extends BristleSecurityException {

  public IncorrectUsernameOrPasswordException(String username) {
    super(username);
  }
}
