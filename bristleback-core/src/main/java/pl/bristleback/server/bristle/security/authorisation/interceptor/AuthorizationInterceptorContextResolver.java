package pl.bristleback.server.bristle.security.authorisation.interceptor;

import pl.bristleback.server.bristle.action.ActionInformation;
import pl.bristleback.server.bristle.api.action.ActionInterceptorContextResolver;
import pl.bristleback.server.bristle.api.annotations.Authorized;

import java.util.Arrays;
import java.util.List;

/**
 * This class helps {@link AuthorizationInterceptor} and resolves required rights for action or action class,
 * using {@link pl.bristleback.server.bristle.api.annotations.Authorized} annotation. Resolved objects are of {@link RequiredRights} type.
 * <p/>
 * Created on: 09.02.13 18:52 <br/>
 *
 * @author Wojciech Niemiec
 */
public class AuthorizationInterceptorContextResolver implements ActionInterceptorContextResolver<RequiredRights> {

  @Override
  public RequiredRights resolveInterceptorContext(ActionInformation actionInformation) {
    Authorized authorizedAnnotation;
    if (actionInformation.getMethod().isAnnotationPresent(Authorized.class)) {
      authorizedAnnotation = actionInformation.getMethod().getAnnotation(Authorized.class);
    } else {
      authorizedAnnotation = actionInformation.getActionClass().getType().getAnnotation(Authorized.class);
    }
    List<String> requiredRights = Arrays.asList(authorizedAnnotation.value());
    return new RequiredRights(requiredRights);
  }
}
