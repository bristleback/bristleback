package pl.bristleback.server.bristle.security.authentication;

import pl.bristleback.server.bristle.action.ActionInformation;
import pl.bristleback.server.bristle.api.action.ActionInterceptorContextResolver;

/**
 * This interceptor context resolver checks if action returns
 * {@link pl.bristleback.server.bristle.api.users.UserDetails} implementation.
 * It also checks and processes action parameters, for example if password should be encoded.
 * <p/>
 * Created on: 17.02.13 10:42 <br/>
 *
 * @author Wojciech Niemiec
 */
public class AuthenticationInterceptorContextResolver implements ActionInterceptorContextResolver<AuthenticationOperationContext> {

  @Override
  public AuthenticationOperationContext resolveInterceptorContext(ActionInformation actionInformation) {
    //TODO-do what this class is supposed to do
    return new AuthenticationOperationContext();
  }
}
