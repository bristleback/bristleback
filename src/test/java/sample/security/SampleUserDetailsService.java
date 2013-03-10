package sample.security;

import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.api.users.UserDetails;
import pl.bristleback.server.bristle.api.users.UserDetailsService;

import java.util.HashMap;
import java.util.Map;

@Component
public class SampleUserDetailsService implements UserDetailsService {

  private static final Map<String, UserDetails> USER_DETAILS_MAP = new HashMap<String, UserDetails>();

  static {
    USER_DETAILS_MAP.put("admin", new SampleUserDetails("admin", "admin_pass", "admin"));
    USER_DETAILS_MAP.put("normal", new SampleUserDetails("normal", "normal_pass"));
  }

  @Override
  public UserDetails getUserDetails(String username) {
    return USER_DETAILS_MAP.get(username);
  }

}
