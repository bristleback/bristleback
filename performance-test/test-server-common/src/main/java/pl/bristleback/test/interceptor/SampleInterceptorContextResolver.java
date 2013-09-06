package pl.bristleback.test.interceptor;

import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.action.ActionInformation;
import pl.bristleback.server.bristle.api.action.ActionInterceptorContextResolver;

@Component
public class SampleInterceptorContextResolver implements ActionInterceptorContextResolver<ActionInterceptorContextObject> {

  @Override
  public ActionInterceptorContextObject resolveInterceptorContext(ActionInformation actionInformation) {
    return new ActionInterceptorContextObject(actionInformation.getMethod().getAnnotations().length);
  }
}
