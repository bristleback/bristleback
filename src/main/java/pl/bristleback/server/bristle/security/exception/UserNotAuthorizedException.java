package pl.bristleback.server.bristle.security.exception;

/**
 * This exception is thrown when user doesn't have one or more rights required by some secured action.
 * <p/>
 * Created on: 08.03.13 18:02 <br/>
 *
 * @author Wojciech Niemiec
 */
public class UserNotAuthorizedException extends RuntimeException {

  private String requiredRight;

  public UserNotAuthorizedException(String requiredRight) {
    this.requiredRight = requiredRight;
  }

  public String getRequiredRight() {
    return requiredRight;
  }
}
