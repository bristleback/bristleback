package pl.bristleback.server.bristle.security.exception;

/**
 * This exception is thrown when user doesn't have one or more rights required in secured action.
 * <p/>
 * Created on: 08.03.13 18:02 <br/>
 *
 * @author Wojciech Niemiec
 */
public class AuthorizationException extends BristleSecurityException {

  private String missingAuthority;

  public AuthorizationException(String username, String missingAuthority) {
    super(username);
    this.missingAuthority = missingAuthority;
  }

  public String getMissingAuthority() {
    return missingAuthority;
  }
}
