package pl.bristleback.server.bristle.validation;

import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.action.ActionInformation;
import pl.bristleback.server.bristle.api.action.ActionInterceptorContextResolver;

import javax.inject.Inject;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

@Component
public class ValidationInterceptorContextResolver implements ActionInterceptorContextResolver<ValidationInterceptorContext> {

  @Inject
  private ValidatorFactory validatorFactory;

  @Override
  public ValidationInterceptorContext resolveInterceptorContext(ActionInformation actionInformation) {
    Validator validator = validatorFactory.getValidator();
    return new ValidationInterceptorContext(validator.forExecutables());
  }
}
