package pl.bristleback.server.bristle.security.exception;

/**
 * //@todo class description
 * <p/>
 * Created on: 19.02.13 22:47 <br/>
 *
 * @author Wojciech Niemiec
 */
public class IncorrectUsernameOrPasswordException extends RuntimeException {

  private String username;

  public IncorrectUsernameOrPasswordException(String username) {
    this.username = username;
  }

  public String getUsername() {
    return username;
  }
}
