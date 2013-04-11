package pl.bristleback.server.bristle.security.authentication;

/**
 * Logout message payload, containing information about username and logout reason.
 * <p/>
 * Created on: 07.04.13 19:31 <br/>
 *
 * @author Wojciech Niemiec
 */
public class LogoutMessagePayload {

  private String username;

  private LogoutReason logoutReason;

  public LogoutMessagePayload(String username, LogoutReason logoutReason) {
    this.username = username;
    this.logoutReason = logoutReason;
  }

  public String getUsername() {
    return username;
  }

  public LogoutReason getLogoutReason() {
    return logoutReason;
  }
}
