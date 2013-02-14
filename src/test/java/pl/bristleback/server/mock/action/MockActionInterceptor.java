package pl.bristleback.server.mock.action;

import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.action.ActionExecutionContext;
import pl.bristleback.server.bristle.action.ActionExecutionStage;
import pl.bristleback.server.bristle.action.ActionInformation;
import pl.bristleback.server.bristle.api.action.ActionInterceptor;
import pl.bristleback.server.bristle.api.annotations.Interceptor;

@Component
@Interceptor(stages = ActionExecutionStage.ACTION_EXECUTION, contextResolver = MockActionInterceptorResolver.class)
public class MockActionInterceptor implements ActionInterceptor<MockInterceptorContext> {

  @Override
  public void intercept(ActionInformation actionInformation, ActionExecutionContext context, MockInterceptorContext interceptorContext) {

  }
}
