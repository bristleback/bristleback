package pl.bristleback.server.bristle.security.exception.response;

import pl.bristleback.server.bristle.action.response.ExceptionResponse;

/**
 * Security exception response is a base response for all security exceptions in Bristleback Security System.
 * <p/>
 * Created on: 30.03.13 09:50 <br/>
 *
 * @author Wojciech Niemiec
 * @see pl.bristleback.server.bristle.security.exception.handler.BristleSecurityExceptionHandler
 */
public class SecurityExceptionResponse extends ExceptionResponse {

  private String username;

  /**
   * Creates new instance of security exception response, containing information about username and simple type of exception.
   *
   * @param username name of the user to which this exception response is addressed.
   * @param type     simple type of the exception class.
   */
  public SecurityExceptionResponse(String username, String type) {
    super(type);
    this.username = username;
  }

  public String getUsername() {
    return username;
  }
}
