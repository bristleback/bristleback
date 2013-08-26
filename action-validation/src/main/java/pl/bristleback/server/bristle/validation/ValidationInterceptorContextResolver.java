package pl.bristleback.server.bristle.validation;

import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.action.ActionInformation;
import pl.bristleback.server.bristle.api.action.ActionInterceptorContextResolver;
import pl.bristleback.server.bristle.validation.init.LazyValidatorFactoryContainer;

import javax.inject.Inject;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

@Component
public class ValidationInterceptorContextResolver implements ActionInterceptorContextResolver<ValidationInterceptorContext> {

  @Inject
  private LazyValidatorFactoryContainer validatorFactoryContainer;

  @Override
  public ValidationInterceptorContext resolveInterceptorContext(ActionInformation actionInformation) {
    ValidatorFactory validatorFactory = validatorFactoryContainer.getOrBuildValidatorFactory();
    Validator validator = validatorFactory.getValidator();
    return new ValidationInterceptorContext(validator.forExecutables());
  }
}
