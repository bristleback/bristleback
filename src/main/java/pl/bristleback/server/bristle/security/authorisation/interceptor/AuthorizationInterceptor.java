package pl.bristleback.server.bristle.security.authorisation.interceptor;

import pl.bristleback.server.bristle.action.ActionExecutionContext;
import pl.bristleback.server.bristle.action.ActionExecutionStage;
import pl.bristleback.server.bristle.action.ActionInformation;
import pl.bristleback.server.bristle.api.action.ActionInterceptor;
import pl.bristleback.server.bristle.api.annotations.Interceptor;
import pl.bristleback.server.bristle.exceptions.UserNotAuthorizedException;
import pl.bristleback.server.bristle.security.authentication.AuthenticationsContainer;
import pl.bristleback.server.bristle.security.authentication.UserAuthentication;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Prototype of Bristleback built in authorization interceptor.
 * This interceptor uses custom intercepting annotation, {@link pl.bristleback.server.bristle.api.annotations.Authorized}.
 * <p/>
 * Created on: 09.02.13 18:49 <br/>
 *
 * @author Wojciech Niemiec
 */
@Interceptor(stages = ActionExecutionStage.ACTION_EXTRACTION, contextResolver = AuthorizationInterceptorContextResolver.class)
public class AuthorizationInterceptor implements ActionInterceptor<RequiredRights> {

  @Inject
  @Named("bristleAuthenticationsContainer")
  private AuthenticationsContainer authenticationsContainer;

  @Override
  public void intercept(ActionInformation actionInformation, ActionExecutionContext context, RequiredRights requiredRights) {
    UserAuthentication authentication = authenticationsContainer.getAuthentication(context.getUserContext().getId());
    for (String requiredRight : requiredRights.getRequiredRights()) {
      if (!authentication.getAuthenticatedUser().getAuthorities().contains(requiredRight)) {
        throw new UserNotAuthorizedException(requiredRight);
      }
    }
  }
}
