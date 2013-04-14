package pl.bristleback.server.bristle.api.users;

import java.util.Collection;

/**
 * This interface describes details of users secured by the Bristleback security system.
 * <p/>
 * Created on: 18.02.13 19:09 <br/>
 *
 * @author Wojciech Niemiec
 */
public interface UserDetails {

  String getUsername();

  String getPassword();

  Collection<String> getAuthorities();

  boolean isAccountNonExpired();

  boolean isAccountNonLocked();

  boolean isCredentialsNonExpired();

  boolean isEnabled();
}
