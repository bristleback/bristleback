package pl.bristleback.server.bristle.security.authentication;

import pl.bristleback.server.bristle.api.annotations.Action;
import pl.bristleback.server.bristle.api.annotations.ActionClass;
import pl.bristleback.server.bristle.api.annotations.Intercept;

/**
 * This is a default logout action class used in system authentication framework.
 * Its only action, {@link pl.bristleback.server.bristle.security.authentication.LogoutAction#logout()}
 * is intercepted by {@link LogoutInterceptor}, which invalidates current authentication from actual connection.
 * <p/>
 * Created on: 18.02.13 20:30 <br/>
 *
 * @author Wojciech Niemiec
 */
@ActionClass(name = "BristleSystemUserLogout")
public class LogoutAction {

  @Intercept(LogoutInterceptor.class)
  @Action
  public void logout() {

  }
}
