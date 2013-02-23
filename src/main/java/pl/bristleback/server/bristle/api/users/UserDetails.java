package pl.bristleback.server.bristle.api.users;

/**
 * //@todo class description
 * <p/>
 * Created on: 18.02.13 19:09 <br/>
 *
 * @author Wojciech Niemiec
 */
public interface UserDetails {

  String getUsername();

  String getPassword();

  boolean isLogged();

  boolean isAccountNonExpired();

  boolean isAccountNonLocked();

  boolean isCredentialsNonExpired();

  boolean isEnabled();
}
