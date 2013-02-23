package pl.bristleback.server.bristle.authentication;

import pl.bristleback.server.bristle.api.users.UserDetails;
import pl.bristleback.server.bristle.engine.base.ConnectedUser;

/**
 * //@todo class description
 * <p/>
 * Created on: 17.02.13 10:43 <br/>
 *
 * @author Wojciech Niemiec
 */
public class UserAuthentication {

  private final ConnectedUser user;

  private final UserDetails authenticatedUser;

  private boolean valid;

  public UserAuthentication(ConnectedUser user, UserDetails authenticatedUser) {
    this.user = user;
    this.authenticatedUser = authenticatedUser;
    valid = true;
  }

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

  public void invalidate() {
    valid = false;
  }
}
