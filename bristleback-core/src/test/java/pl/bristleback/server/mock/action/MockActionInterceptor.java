package pl.bristleback.server.mock.action;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.action.ActionExecutionContext;
import pl.bristleback.server.bristle.action.ActionExecutionStage;
import pl.bristleback.server.bristle.action.ActionInformation;
import pl.bristleback.server.bristle.api.action.ActionInterceptor;
import pl.bristleback.server.bristle.api.annotations.Interceptor;

@Component
@Interceptor(stages = ActionExecutionStage.ACTION_EXECUTION, contextResolver = MockActionInterceptorResolver.class)
public class MockActionInterceptor implements ActionInterceptor<MockInterceptorContext> {

  private static Logger log = Logger.getLogger(MockActionInterceptor.class.getName());

  @Override
  public void intercept(ActionInformation actionInformation, ActionExecutionContext context, MockInterceptorContext interceptorContext) {
    log.debug("interceptor works");
  }
}
