package pl.bristleback.server.bristle.authentication;

import org.springframework.stereotype.Component;

/**
 * //@todo class description
 * <p/>
 * Created on: 18.02.13 19:14 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component
public class AuthenticationConfiguration {

  private int maximumAuthenticationsPerUsername = 2;

  public int getMaximumAuthenticationsPerUsername() {
    return maximumAuthenticationsPerUsername;
  }

  public void setMaximumAuthenticationsPerUsername(int maximumAuthenticationsPerUsername) {
    this.maximumAuthenticationsPerUsername = maximumAuthenticationsPerUsername;
  }
}
