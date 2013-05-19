package pl.bristleback.server.mock.action;

import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.action.ActionInformation;
import pl.bristleback.server.bristle.api.action.ActionInterceptorContextResolver;

@Component
public class MockActionInterceptorResolver implements ActionInterceptorContextResolver<MockInterceptorContext> {

  @Override
  public MockInterceptorContext resolveInterceptorContext(ActionInformation actionInformation) {
    return new MockInterceptorContext();
  }
}
