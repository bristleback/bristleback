package pl.bristleback.server.bristle.security.authentication;

/**
 * This class contains configuration used by the system authentication framework.
 * <p/>
 * Created on: 18.02.13 19:14 <br/>
 *
 * @author Wojciech Niemiec
 */
public class AuthenticationConfiguration {

  private int maximumAuthenticationsPerUsername = 2;

  public int getMaximumAuthenticationsPerUsername() {
    return maximumAuthenticationsPerUsername;
  }

  public void setMaximumAuthenticationsPerUsername(int maximumAuthenticationsPerUsername) {
    this.maximumAuthenticationsPerUsername = maximumAuthenticationsPerUsername;
  }
}
