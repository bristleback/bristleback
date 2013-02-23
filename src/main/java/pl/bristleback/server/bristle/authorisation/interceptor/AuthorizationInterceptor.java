package pl.bristleback.server.bristle.authorisation.interceptor;

import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.action.ActionExecutionContext;
import pl.bristleback.server.bristle.action.ActionExecutionStage;
import pl.bristleback.server.bristle.action.ActionInformation;
import pl.bristleback.server.bristle.api.action.ActionInterceptor;
import pl.bristleback.server.bristle.api.annotations.Interceptor;

/**
 * Prototype of Bristleback built in authorization interceptor.
 * This interceptor uses custom intercepting annotation, {@link pl.bristleback.server.bristle.api.annotations.Authorized}.
 * <p/>
 * Created on: 09.02.13 18:49 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component
@Interceptor(stages = ActionExecutionStage.ACTION_EXTRACTION, contextResolver = AuthorizationInterceptorContextResolver.class)
public class AuthorizationInterceptor implements ActionInterceptor<RequiredRights> {

  @Override
  public void intercept(ActionInformation actionInformation, ActionExecutionContext context, RequiredRights interceptorContext) {
    throw new RuntimeException("you shall not pass");
  }
}
