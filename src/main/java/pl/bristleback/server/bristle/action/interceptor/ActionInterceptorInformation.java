package pl.bristleback.server.bristle.action.interceptor;

import pl.bristleback.server.bristle.api.action.ActionInterceptor;

/**
 * This class is a container used in action interception process.
 * It contains interceptor instance and interception process context.
 * Objects of this class are created for each interception process definition
 * (but not for each interception operation).
 * <p/>
 * Created on: 04.02.13 18:37 <br/>
 *
 * @author Wojciech Niemiec
 */
public class ActionInterceptorInformation {

  private ActionInterceptor interceptorInstance;

  public ActionInterceptorInformation(ActionInterceptor interceptorInstance) {
    this.interceptorInstance = interceptorInstance;
  }

  public ActionInterceptor getInterceptorInstance() {
    return interceptorInstance;
  }
}
