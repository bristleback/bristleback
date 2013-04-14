package pl.bristleback.server.bristle.api.action;

import pl.bristleback.server.bristle.action.ActionExecutionContext;
import pl.bristleback.server.bristle.action.ActionInformation;

/**
 * Every action can be intercepted by any number of action interceptors. To be considered as an interceptor,
 * beans must be annotated with {@link pl.bristleback.server.bristle.api.annotations.Interceptor}.
 * Interception context object is resolved by the {@link ActionInterceptorContextResolver},
 * defined in {@link pl.bristleback.server.bristle.api.annotations.Intercept} annotation.
 * <p/>
 * Created on: 20.01.13 11:52 <br/>
 *
 * @author Wojciech Niemiec
 */
public interface ActionInterceptor<T> {

  /**
   * Intercepts action execution. The latter execution stage is, the more information is available in {@link ActionExecutionContext} object.
   * Interceptor may modify or change various action execution information, as well as cancel response sending by calling
   * {@link pl.bristleback.server.bristle.action.ActionExecutionContext#cancelResponseSending()} method.
   *
   * @param actionInformation  meta information about executed action.
   * @param context            action execution context object, created for each action execution.
   * @param interceptorContext interception execution context, created for each interception operation.
   */
  void intercept(ActionInformation actionInformation, ActionExecutionContext context, T interceptorContext);
}
