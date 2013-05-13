package pl.bristleback.server.mock.action;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.action.ActionExecutionContext;
import pl.bristleback.server.bristle.action.ActionExecutionStage;
import pl.bristleback.server.bristle.action.ActionInformation;
import pl.bristleback.server.bristle.api.action.ActionInterceptor;
import pl.bristleback.server.bristle.api.action.ActionInterceptorContextResolver;
import pl.bristleback.server.bristle.api.annotations.Interceptor;

import javax.inject.Inject;

@Interceptor(stages = ActionExecutionStage.ACTION_EXECUTION)
public class MockActionInterceptor implements ActionInterceptor<MockInterceptorContext> {

  private static Logger log = Logger.getLogger(MockActionInterceptor.class.getName());

  private MockActionInterceptorResolver mockActionInterceptorResolver;

  @Override
  public void intercept(ActionInformation actionInformation, ActionExecutionContext context, MockInterceptorContext interceptorContext) {
    log.debug("interceptor works");
  }

  @Override
  public ActionInterceptorContextResolver<MockInterceptorContext> getContextResolver() {
    return mockActionInterceptorResolver;
  }

  public void setMockActionInterceptorResolver(MockActionInterceptorResolver mockActionInterceptorResolver) {
    this.mockActionInterceptorResolver = mockActionInterceptorResolver;
  }
}
