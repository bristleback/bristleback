package sample.security;

import pl.bristleback.server.bristle.api.users.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

public class SampleUserDetails implements UserDetails {

  private String username;

  private String password;

  private Collection<String> authorities;

  public SampleUserDetails(String username, String password, String... authorities) {
    this.username = username;
    this.password = password;
    this.authorities = new HashSet<String>(Arrays.asList(authorities));
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public Collection<String> getAuthorities() {
    return authorities;
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
