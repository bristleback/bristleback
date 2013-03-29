package pl.bristleback.server.bristle.conf.resolver.action.interceptor;

import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.action.ActionInformation;
import pl.bristleback.server.bristle.action.interceptor.ActionInterceptorInformation;
import pl.bristleback.server.bristle.action.interceptor.InterceptionProcessContext;
import pl.bristleback.server.bristle.api.action.ActionInterceptor;
import pl.bristleback.server.bristle.conf.BristleInitializationException;
import pl.bristleback.server.bristle.utils.ReflectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to resolve {@link InterceptionProcessContext} list,
 * where each context object is responsible for single interception operation.
 * <p/>
 * Created on: 08.02.13 17:40 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component
public class ActionInterceptorContextsResolver {

  public List<InterceptionProcessContext> resolveContexts(List<ActionInterceptorInformation> informationList, ActionInformation actionInformation) {
    List<InterceptionProcessContext> contextList = new ArrayList<InterceptionProcessContext>();
    for (ActionInterceptorInformation interceptorInformation : informationList) {
      Object contextObject = getContextObjectUsingActionInformation(interceptorInformation, actionInformation);
      contextList.add(new InterceptionProcessContext(contextObject, interceptorInformation));
    }

    return contextList;
  }

  private Object getContextObjectUsingActionInformation(ActionInterceptorInformation interceptorInformation, ActionInformation actionInformation) {
    Object contextObject = interceptorInformation.getInterceptorContextResolver().resolveInterceptorContext(actionInformation);
    checkContextObjectCompatibility(contextObject, interceptorInformation);
    return contextObject;
  }

  private void checkContextObjectCompatibility(Object contextObject, ActionInterceptorInformation interceptorInformation) {
    Class<?> interceptorInstanceType = interceptorInformation.getInterceptorInstance().getClass();
    Class<?> interceptorContextObjectType = (Class) ReflectionUtils
      .getParameterTypes(interceptorInstanceType, ActionInterceptor.class)[0];

    if (!interceptorContextObjectType.isAssignableFrom(contextObject.getClass())) {
      throw new BristleInitializationException("Object returned by "
        + interceptorInformation.getInterceptorContextResolver().getClass().getName()
        + " context resolver does not match context parameter type in " + interceptorInstanceType.getName()
        + " interceptor class.");
    }
  }
}
