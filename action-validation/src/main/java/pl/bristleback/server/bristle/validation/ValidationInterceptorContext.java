package pl.bristleback.server.bristle.validation;

import javax.validation.executable.ExecutableValidator;

public class ValidationInterceptorContext {

  private ExecutableValidator validator;

  public ValidationInterceptorContext(ExecutableValidator validator) {
    this.validator = validator;
  }

  public ExecutableValidator getValidator() {
    return validator;
  }
}
