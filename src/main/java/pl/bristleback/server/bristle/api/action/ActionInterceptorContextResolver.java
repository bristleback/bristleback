package pl.bristleback.server.bristle.api.action;

import pl.bristleback.server.bristle.action.ActionInformation;

/**
 * This helper interface serves as a interceptor context object provider for each interception operation type.
 * Implementations must be parametrized with the same type as interceptors they are serving.
 * <p/>
 * Created on: 04.02.13 19:52 <br/>
 *
 * @author Wojciech Niemiec
 */
public interface ActionInterceptorContextResolver<T> {

  /**
   * Prepares interception context object for given action.
   * Created object is passed as a parameter to
   * {@link ActionInterceptor} interface implementation served by this context resolver.
   *
   * @param actionInformation information about intercepted action.
   * @return interception operation context object.
   */
  T resolveInterceptorContext(ActionInformation actionInformation);
}
