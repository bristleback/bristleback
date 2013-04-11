package pl.bristleback.server.bristle.security.exception;

/**
 * This simple exception wraps all situations when username and password are correct but
 * authentication process cannot be completed because of one of the following issues:
 * <ul>
 *   <li>user account is locked ({@link pl.bristleback.server.bristle.api.users.UserDetails#isAccountNonLocked userDetails.isAccountNonLocked()} returns false)</li>
 *   <li>user account has expired ({@link pl.bristleback.server.bristle.api.users.UserDetails#isAccountNonExpired userDetails.isAccountNonExpired()} returns false)</li>
 *   <li>user account is not enabled ({@link pl.bristleback.server.bristle.api.users.UserDetails#isEnabled userDetails.isEnabled()} returns false)</li>
 *   <li>user credentials have expired ({@link pl.bristleback.server.bristle.api.users.UserDetails#isCredentialsNonExpired userDetails.isCredentialsNonExpired()} returns false)</li>
 * </ul>
 * <p/>
 * Created on: 23.02.13 08:44 <br/>
 *
 * @author Wojciech Niemiec
 */
public class InactiveUserException extends BristleSecurityException {

  public InactiveUserException(String username) {
    super(username);
  }
}
