package pl.bristleback.server.bristle.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import pl.bristleback.server.bristle.api.annotations.ActionClass;
import pl.bristleback.server.bristle.api.annotations.Intercept;

/**
 * //@todo class description
 * <p/>
 * Created on: 18.02.13 20:30 <br/>
 *
 * @author Wojciech Niemiec
 */
@Controller
@ActionClass(name = "BristlebackSystemLogoutUser")
public class LogoutAction {

  @Autowired
  private AuthenticationsContainer authenticationsContainer;

  @Intercept(LogoutInterceptor.class)
  public void logout() {

  }
}
