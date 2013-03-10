package pl.bristleback.server.bristle.api.users;

import java.util.Collection;

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

  Collection<String> getAuthorities();

  boolean isLogged();

  boolean isAccountNonExpired();

  boolean isAccountNonLocked();

  boolean isCredentialsNonExpired();

  boolean isEnabled();
}
