package pl.bristleback.server.bristle.security.authentication;

import org.apache.log4j.Logger;
import pl.bristleback.server.bristle.action.ActionExecutionContext;
import pl.bristleback.server.bristle.action.ActionExecutionStage;
import pl.bristleback.server.bristle.action.ActionInformation;
import pl.bristleback.server.bristle.api.BristlebackConfig;
import pl.bristleback.server.bristle.api.ConfigurationAware;
import pl.bristleback.server.bristle.api.action.ActionInterceptor;
import pl.bristleback.server.bristle.api.annotations.Interceptor;
import pl.bristleback.server.bristle.api.users.UserDetails;
import pl.bristleback.server.bristle.exceptions.UserAlreadyAuthenticatedException;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * This class intercepts response from authenticating actions
 * and adds new authentication objects assigned for this connection.
 * If the number of concurrent connections authenticated as the same user as currently processed is exceeded,
 * the oldest one will be invalidated.
 * <p/>
 * Created on: 17.02.13 10:22 <br/>
 *
 * @author Wojciech Niemiec
 */
@Interceptor(stages = ActionExecutionStage.ACTION_EXECUTION, contextResolver = AuthenticationInterceptorContextResolver.class)
public class AuthenticationInterceptor implements ActionInterceptor<AuthenticationOperationContext>, ConfigurationAware {

  private static Logger log = Logger.getLogger(AuthenticationInterceptor.class.getName());

  @Inject
  @Named("bristleAuthenticationsContainer")
  private AuthenticationsContainer authenticationsContainer;

  @Override
  public void init(BristlebackConfig configuration) {
  }

  @Override
  public void intercept(ActionInformation actionInformation, ActionExecutionContext context, AuthenticationOperationContext interceptorContext) {
    if (authenticationsContainer.hasValidAuthenticationForConnection(context.getUser().getId())) {
      throw new UserAlreadyAuthenticatedException();
    }
    UserDetails userDetails = (UserDetails) context.getResponse();
    UserAuthentication userAuthentication = UserAuthentication.newValidAuthentication(context.getUser(), userDetails);
    authenticationsContainer.addAndInvalidatePreviousIfNecessary(userAuthentication);
    context.cancelResponseSending();
    log.debug("User \"" + userDetails.getUsername() + "\" has been successfully authenticated.");
  }

}
