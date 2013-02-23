package pl.bristleback.server.bristle.authentication;

import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.action.ActionInformation;
import pl.bristleback.server.bristle.api.action.ActionInterceptorContextResolver;

/**
 * //@todo class description
 * <p/>
 * Created on: 17.02.13 10:42 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component
public class AuthenticationInterceptorContextResolver implements ActionInterceptorContextResolver<AuthenticationOperationContext> {

  @Override
  public AuthenticationOperationContext resolveInterceptorContext(ActionInformation actionInformation) {
    return new AuthenticationOperationContext();
  }
}
