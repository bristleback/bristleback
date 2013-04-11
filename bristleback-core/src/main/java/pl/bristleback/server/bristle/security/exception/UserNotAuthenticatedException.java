package pl.bristleback.server.bristle.security.exception;

/**
 * This exception is thrown whenever application or authentication system want to retrieve authentication object
 * assigned to active user connection and that authentication cannot be found or is no longer valid
 * ({@link pl.bristleback.server.bristle.security.authentication.UserAuthentication#isValid()} returns false).
 * <p/>
 * Created on: 18.02.13 21:14 <br/>
 *
 * @author Wojciech Niemiec
 */
public class UserNotAuthenticatedException extends BristleSecurityException {

  public UserNotAuthenticatedException() {
    super();
  }
}
