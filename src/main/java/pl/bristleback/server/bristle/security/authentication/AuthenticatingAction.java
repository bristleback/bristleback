package pl.bristleback.server.bristle.security.authentication;

import pl.bristleback.server.bristle.api.annotations.Action;
import pl.bristleback.server.bristle.api.annotations.ActionClass;
import pl.bristleback.server.bristle.api.annotations.Authenticator;
import pl.bristleback.server.bristle.api.users.UserDetails;
import pl.bristleback.server.bristle.api.users.UserDetailsService;
import pl.bristleback.server.bristle.security.exception.InactiveUserException;
import pl.bristleback.server.bristle.security.exception.IncorrectUsernameOrPasswordException;

/**
 * This is a system authentication action class, intercepted by {@link Authenticator}.
 * This action relies on {@link UserDetailsService} implementation provided by application creator.
 * Note that it is possible to create multiple authenticating action classes.
 * To be usable by the authentication framework, their action must be annotated with {@link Authenticator}
 * and their return type must be {@link UserDetails} implementation.
 * <p/>
 * Created on: 19.02.13 22:43 <br/>
 *
 * @author Wojciech Niemiec
 */
@ActionClass(name = "BristleSystemUserAuthentication")
public class AuthenticatingAction {

  private UserDetailsService userDetailsService;

  @Action
  @Authenticator
  public UserDetails authenticate(String username, String password) {
    UserDetails userDetails = userDetailsService.getUserDetails(username);
    if (userDetails == null || !userDetails.getPassword().equals(password)) {
      throw new IncorrectUsernameOrPasswordException(username);
    }
    if (!userDetails.isAccountNonLocked() || !userDetails.isAccountNonExpired()
      || !userDetails.isEnabled() || !userDetails.isCredentialsNonExpired()) {
      throw new InactiveUserException();
    }

    return userDetails;
  }

  /**
   * {@link UserDetailsService} implementation must be provided by application creator and will be injected into this action class.
   *
   * @param userDetailsService user details service implementation.
   */
  public void setUserDetailsService(UserDetailsService userDetailsService) {
    this.userDetailsService = userDetailsService;
  }
}
