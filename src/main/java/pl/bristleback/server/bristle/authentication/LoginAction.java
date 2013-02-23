package pl.bristleback.server.bristle.authentication;

import pl.bristleback.server.bristle.api.annotations.Action;
import pl.bristleback.server.bristle.api.annotations.ActionClass;
import pl.bristleback.server.bristle.api.annotations.Authenticator;
import pl.bristleback.server.bristle.api.users.UserDetails;
import pl.bristleback.server.bristle.api.users.UserDetailsService;
import pl.bristleback.server.bristle.exceptions.InactiveUserException;
import pl.bristleback.server.bristle.exceptions.IncorrectUsernameOrPasswordException;

/**
 * //@todo class description
 * <p/>
 * Created on: 19.02.13 22:43 <br/>
 *
 * @author Wojciech Niemiec
 */
@ActionClass
public class LoginAction {

  private UserDetailsService userDetailsService;

  @Action
  @Authenticator
  public UserDetails authenticate(String username, String password) {
    UserDetails userDetails = userDetailsService.getUserDetails(username);
    if (userDetails == null) {
      throw new IncorrectUsernameOrPasswordException(username);
    }
    if (!userDetails.isAccountNonLocked() || !userDetails.isAccountNonExpired()
      || !userDetails.isEnabled() || !userDetails.isCredentialsNonExpired()) {
      throw new InactiveUserException();
    }
    if (!userDetails.getPassword().equals(password)) {
      throw new IncorrectUsernameOrPasswordException(username);
    }
    return userDetails;
  }

  public void setUserDetailsService(UserDetailsService userDetailsService) {
    this.userDetailsService = userDetailsService;
  }
}
