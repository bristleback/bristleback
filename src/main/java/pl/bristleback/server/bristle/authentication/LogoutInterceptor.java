package pl.bristleback.server.bristle.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import pl.bristleback.server.bristle.action.ActionExecutionContext;
import pl.bristleback.server.bristle.action.ActionInformation;
import pl.bristleback.server.bristle.api.action.ActionInterceptor;

/**
 * //@todo class description
 * <p/>
 * Created on: 18.02.13 20:57 <br/>
 *
 * @author Wojciech Niemiec
 */
public class LogoutInterceptor implements ActionInterceptor<AuthenticationOperationContext> {

  @Autowired
    private AuthenticationsContainer authenticationsContainer;

  @Override
  public void intercept(ActionInformation actionInformation, ActionExecutionContext context, AuthenticationOperationContext interceptorContext) {
    authenticationsContainer.logout(context.getConnectedUser());
  }
}
