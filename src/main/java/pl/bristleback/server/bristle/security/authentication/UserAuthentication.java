package pl.bristleback.server.bristle.security.authentication;

import pl.bristleback.server.bristle.api.users.UserContext;
import pl.bristleback.server.bristle.api.users.UserDetails;

/**
 * Abstraction for single user authentication. Binds connection with user details.
 * <p/>
 * Created on: 17.02.13 10:43 <br/>
 *
 * @author Wojciech Niemiec
 */
public final class UserAuthentication {


  private final UserContext userContext;

  private final UserDetails authenticatedUser;

  private boolean valid;

  private UserAuthentication(UserContext userContext, UserDetails authenticatedUser) {
    this.userContext = userContext;
    this.authenticatedUser = authenticatedUser;
    valid = true;
  }

  /**
   * Creates new valid authentication.
   *
   * @param userContext user context object.
   * @param userDetails details of logged user.
   * @return valid authentication.
   */
  public static UserAuthentication newValidAuthentication(UserContext userContext, UserDetails userDetails) {
    return new UserAuthentication(userContext, userDetails);
  }

  public boolean isValid() {
    return valid;
  }

  public UserDetails getAuthenticatedUser() {
    return authenticatedUser;
  }

  public UserContext getUserContext() {
    return userContext;
  }

  /**
   * Invalidate this authentication, making it not usable in secured actions.
   */
  public void invalidate() {
    valid = false;
  }
}
