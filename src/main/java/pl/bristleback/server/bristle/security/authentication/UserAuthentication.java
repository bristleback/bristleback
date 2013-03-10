package pl.bristleback.server.bristle.security.authentication;

import pl.bristleback.server.bristle.api.users.UserDetails;
import pl.bristleback.server.bristle.engine.base.ConnectedUser;

/**
 * Abstraction for single user authentication. Binds connection with user details.
 * <p/>
 * Created on: 17.02.13 10:43 <br/>
 *
 * @author Wojciech Niemiec
 */
public final class UserAuthentication {

  private final ConnectedUser user;

  private final UserDetails authenticatedUser;

  private boolean valid;

  private UserAuthentication(ConnectedUser user, UserDetails authenticatedUser) {
    this.user = user;
    this.authenticatedUser = authenticatedUser;
    valid = true;
  }

  /**
   * Creates new valid authentication.
   *
   * @param user        connection representation.
   * @param userDetails details of logged user.
   * @return valid authentication.
   */
  public static UserAuthentication newValidAuthentication(ConnectedUser user, UserDetails userDetails) {
    return new UserAuthentication(user, userDetails);
  }

  public boolean isValid() {
    return valid;
  }

  public ConnectedUser getUser() {
    return user;
  }

  public UserDetails getAuthenticatedUser() {
    return authenticatedUser;
  }

  /**
   * Invalidate this authentication, making it not usable in secured actions.
   */
  public void invalidate() {
    valid = false;
  }
}
