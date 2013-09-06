package pl.bristleback.test.interceptor;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.action.ActionExecutionContext;
import pl.bristleback.server.bristle.action.ActionExecutionStage;
import pl.bristleback.server.bristle.api.action.ActionInterceptor;
import pl.bristleback.server.bristle.api.action.ActionInterceptorContextResolver;
import pl.bristleback.server.bristle.api.annotations.Interceptor;

import javax.inject.Inject;

@Interceptor(stages = ActionExecutionStage.ACTION_EXTRACTION)
@Component
public class SampleInterceptor implements ActionInterceptor<ActionInterceptorContextObject> {

  private static Logger log = Logger.getLogger(SampleInterceptor.class.getName());

  @Inject
  private SampleInterceptorContextResolver sampleInterceptorContextResolver;

  @Override
  public void intercept(ActionExecutionContext context, ActionInterceptorContextObject interceptorContext) {
    log.debug("interceptor works");
  }

  @Override
  public ActionInterceptorContextResolver<ActionInterceptorContextObject> getContextResolver() {
    return sampleInterceptorContextResolver;
  }
}
