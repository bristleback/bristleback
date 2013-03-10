package pl.bristleback.server.mock.security;

import pl.bristleback.server.bristle.api.users.UserDetails;
import pl.bristleback.server.bristle.api.users.UserDetailsService;

public class SampleUserDetailsService implements UserDetailsService {

  @Override
  public UserDetails getUserDetails(String username) {
    return null;
  }
}
