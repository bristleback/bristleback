package pl.bristleback.server.bristle.validation;

import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.action.ActionInformation;
import pl.bristleback.server.bristle.api.action.ActionInterceptorContextResolver;

@Component
public class ValidationInterceptorContextResolver implements ActionInterceptorContextResolver<ValidationInterceptorContext> {

  public ValidationInterceptorContextResolver() {
    System.out.println("sdsd");
  }

  @Override
  public ValidationInterceptorContext resolveInterceptorContext(ActionInformation actionInformation) {
    return new ValidationInterceptorContext();
  }
}
