package sample.action.interceptor;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.action.ActionExecutionContext;
import pl.bristleback.server.bristle.action.ActionExecutionStage;
import pl.bristleback.server.bristle.action.ActionInformation;
import pl.bristleback.server.bristle.api.action.ActionInterceptor;
import pl.bristleback.server.bristle.api.annotations.Interceptor;

/**
 * //@todo class description
 * <p/>
 * Created on: 24.01.13 21:23 <br/>
 *
 * @author Wojciech Niemiec
 */
@Interceptor(stages = ActionExecutionStage.ACTION_EXTRACTION, contextResolver = SampleInterceptorContextResolver.class)
@Component
public class SampleInterceptor implements ActionInterceptor<ActionInterceptorContextObject> {

  private static Logger log = Logger.getLogger(SampleInterceptor.class.getName());

  @Override
  public void intercept(ActionInformation actionInformation, ActionExecutionContext context, ActionInterceptorContextObject interceptorContext) {
    log.debug("interceptor works");
  }
}
