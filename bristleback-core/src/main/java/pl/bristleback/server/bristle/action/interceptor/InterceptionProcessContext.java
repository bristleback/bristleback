package pl.bristleback.server.bristle.action.interceptor;

import pl.bristleback.server.bristle.action.ActionExecutionContext;
import pl.bristleback.server.bristle.action.ActionInformation;

/**
 * This class is a container used in action interception process.
 * It contains interceptor context object and interceptor information.
 * Objects of this class are created for each interception process definition
 * (but not for each interception operation).
 * <p/>
 * Created on: 04.02.13 20:06 <br/>
 *
 * @author Wojciech Niemiec
 */
public class InterceptionProcessContext {

  private Object interceptorContext;

  private ActionInterceptorInformation interceptorInformation;

  public InterceptionProcessContext(Object interceptorContext, ActionInterceptorInformation interceptorInformation) {
    this.interceptorContext = interceptorContext;
    this.interceptorInformation = interceptorInformation;
  }

  public void intercept(ActionInformation actionInformation, ActionExecutionContext context) {
    interceptorInformation.getInterceptorInstance().intercept(actionInformation, context, interceptorContext);
  }

  public ActionInterceptorInformation getInterceptorInformation() {
    return interceptorInformation;
  }
}
