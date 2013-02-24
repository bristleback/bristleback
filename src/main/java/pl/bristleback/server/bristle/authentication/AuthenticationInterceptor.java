package pl.bristleback.server.bristle.authentication;

import pl.bristleback.server.bristle.action.ActionExecutionContext;
import pl.bristleback.server.bristle.action.ActionExecutionStage;
import pl.bristleback.server.bristle.action.ActionInformation;
import pl.bristleback.server.bristle.api.BristlebackConfig;
import pl.bristleback.server.bristle.api.ConfigurationAware;
import pl.bristleback.server.bristle.api.action.ActionInterceptor;
import pl.bristleback.server.bristle.api.annotations.Interceptor;
import pl.bristleback.server.bristle.api.users.UserDetails;
import pl.bristleback.server.bristle.conf.UserConfiguration;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * //@todo class description
 * <p/>
 * Created on: 17.02.13 10:22 <br/>
 *
 * @author Wojciech Niemiec
 */
@Interceptor(stages = ActionExecutionStage.RESPONSE_CONSTRUCTION, contextResolver = AuthenticationInterceptorContextResolver.class)
public class AuthenticationInterceptor implements ActionInterceptor<AuthenticationOperationContext>, ConfigurationAware {

  private UserConfiguration userConfiguration;

  @Inject
  @Named("bristleAuthenticationsContainer")
  private AuthenticationsContainer authenticationsContainer;

  @Override
  public void init(BristlebackConfig configuration) {
    //TODO- check if user is of pl.bristleback.server.bristle.api.users.UserDetails type
    userConfiguration = configuration.getUserConfiguration();
  }

  @Override
  public void intercept(ActionInformation actionInformation, ActionExecutionContext context, AuthenticationOperationContext interceptorContext) {
    UserDetails userDetails = (UserDetails) context.getResponse();
    UserAuthentication userAuthentication = UserAuthentication.newValidAuthentication(context.getConnectedUser(), userDetails);
    authenticationsContainer.addAndInvalidatePreviousIfNecessary(userAuthentication);

  }

}
