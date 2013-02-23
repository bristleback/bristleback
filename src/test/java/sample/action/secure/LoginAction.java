package sample.action.secure;

import pl.bristleback.server.bristle.api.annotations.Action;
import pl.bristleback.server.bristle.api.annotations.ActionClass;
import pl.bristleback.server.bristle.api.annotations.Authenticator;
import sample.user.AuthenticatedSampleUserDetails;

/**
 * //@todo class description
 * <p/>
 * Created on: 17.02.13 14:52 <br/>
 *
 * @author Wojciech Niemiec
 */
@ActionClass
public class LoginAction {

  @Action
  @Authenticator
  public AuthenticatedSampleUserDetails login(String login, String password) {
    return new AuthenticatedSampleUserDetails(login, password);
  }
}
