package sample.user;

import pl.bristleback.server.bristle.api.users.UserDetails;

/**
 * //@todo class description
 * <p/>
 * Created on: 18.02.13 20:09 <br/>
 *
 * @author Wojciech Niemiec
 */
public class AuthenticatedSampleUserDetails implements UserDetails {

  private final String login;

  private final String password;

  public AuthenticatedSampleUserDetails(String login, String password) {
    this.login = login;
    this.password = password;
  }

  @Override
  public String getUsername() {
    return login;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public boolean isLogged() {
    return true;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
