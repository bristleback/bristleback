package pl.bristleback.server.bristle.security.authentication;

/**
 * This class contains configuration used by the system authentication framework.
 * <p/>
 * Created on: 18.02.13 19:14 <br/>
 *
 * @author Wojciech Niemiec
 */
public class AuthenticationConfiguration {

  private int maximumAuthenticationsPerUsername;

  /**
   * Gets maximum concurrent, valid authentications for each username. If not specified by application creator,
   * value of this property is equal to 0, which means there is no limit of concurrent valid authentications.
   *
   * @return maximum concurrent authentications registered for each username.
   */
  public int getMaximumAuthenticationsPerUsername() {
    return maximumAuthenticationsPerUsername;
  }

  /**
   * Sets maximum concurrent authentications registered for each username. If specified value is set to 0,
   * then no limit of concurrent valid authentications is applied.
   *
   * @param maximumAuthenticationsPerUsername
   *         maximum number of concurrent authentications.
   */
  public void setMaximumAuthenticationsPerUsername(int maximumAuthenticationsPerUsername) {
    this.maximumAuthenticationsPerUsername = maximumAuthenticationsPerUsername;
  }
}
