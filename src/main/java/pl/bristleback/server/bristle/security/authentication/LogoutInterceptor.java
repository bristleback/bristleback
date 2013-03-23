package pl.bristleback.server.bristle.security.authentication;

import org.apache.log4j.Logger;
import pl.bristleback.server.bristle.action.ActionExecutionContext;
import pl.bristleback.server.bristle.action.ActionExecutionStage;
import pl.bristleback.server.bristle.action.ActionInformation;
import pl.bristleback.server.bristle.api.action.ActionInterceptor;
import pl.bristleback.server.bristle.api.annotations.Interceptor;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Logout interceptor handles logout process, invalidates current authentication
 * and removes it from concurrent authentications for this username.
 * <p/>
 * Created on: 18.02.13 20:57 <br/>
 *
 * @author Wojciech Niemiec
 */
@Interceptor(stages = ActionExecutionStage.ACTION_EXECUTION, contextResolver = AuthenticationInterceptorContextResolver.class)
public class LogoutInterceptor implements ActionInterceptor<AuthenticationOperationContext> {

  private static Logger log = Logger.getLogger(LogoutInterceptor.class.getName());

  @Inject
  @Named("bristleAuthenticationsContainer")
  private AuthenticationsContainer authenticationsContainer;

  @Override
  public void intercept(ActionInformation actionInformation, ActionExecutionContext context, AuthenticationOperationContext interceptorContext) {
    String connectionId = context.getUserContext().getId();
    String username = authenticationsContainer.getAuthentication(connectionId).getAuthenticatedUser().getUsername();
    authenticationsContainer.logout(connectionId);
    log.debug("User \"" + username + "\" has been logged out.");
  }
}
