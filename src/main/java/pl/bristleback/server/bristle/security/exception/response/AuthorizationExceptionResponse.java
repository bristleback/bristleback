package pl.bristleback.server.bristle.security.exception.response;

import pl.bristleback.server.bristle.security.exception.AuthorizationException;

/**
 * Authorization exception response is a extension for {@link SecurityExceptionResponse} response,
 * adds information which authority is missing. This information is stored in <code>authority</code> field.
 * <p/>
 * Created on: 30.03.13 14:13 <br/>
 *
 * @author Wojciech Niemiec
 */
public class AuthorizationExceptionResponse extends SecurityExceptionResponse {

  private String authority;

  /**
   * Creates new instance of authorization exception response, containing information about username and missing authority.
   *
   * @param username  ame of the user to which this exception response is addressed.
   * @param authority missing authority.
   */
  public AuthorizationExceptionResponse(String username, String authority) {
    super(username, AuthorizationException.class.getSimpleName());
    this.authority = authority;
  }

  public String getAuthority() {
    return authority;
  }
}
