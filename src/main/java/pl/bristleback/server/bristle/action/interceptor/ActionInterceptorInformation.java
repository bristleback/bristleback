package pl.bristleback.server.bristle.action.interceptor;

import pl.bristleback.server.bristle.action.ActionExecutionStage;
import pl.bristleback.server.bristle.api.action.ActionInterceptor;
import pl.bristleback.server.bristle.api.action.ActionInterceptorContextResolver;

/**
 * This class contains information about single action interceptor.
 * It contains interceptor instance, interception process context object resolver
 * and information about action stages to which this interceptor applies.
 * <p/>
 * Created on: 04.02.13 18:37 <br/>
 *
 * @author Wojciech Niemiec
 */
public class ActionInterceptorInformation {

  private ActionInterceptor interceptorInstance;

  private ActionInterceptorContextResolver interceptorContextResolver;

  private ActionExecutionStage[] stages;

  public ActionInterceptorInformation(ActionInterceptor interceptorInstance,
                                      ActionInterceptorContextResolver interceptorContextResolver,
                                      ActionExecutionStage[] stages) {
    this.interceptorInstance = interceptorInstance;
    this.interceptorContextResolver = interceptorContextResolver;
    this.stages = stages;
  }

  public ActionInterceptorInformation(ActionInterceptor interceptorInstance, ActionExecutionStage[] stages) {
    this.interceptorInstance = interceptorInstance;
    this.stages = stages;
  }

  public ActionInterceptor getInterceptorInstance() {
    return interceptorInstance;
  }

  public ActionInterceptorContextResolver getInterceptorContextResolver() {
    return interceptorContextResolver;
  }

  public ActionExecutionStage[] getStages() {
    return stages;
  }
}
