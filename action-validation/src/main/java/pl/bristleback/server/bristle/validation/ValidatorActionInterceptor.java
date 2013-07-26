package pl.bristleback.server.bristle.validation;

import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.action.ActionExecutionContext;
import pl.bristleback.server.bristle.action.ActionExecutionStage;
import pl.bristleback.server.bristle.api.action.ActionInterceptor;
import pl.bristleback.server.bristle.api.action.ActionInterceptorContextResolver;
import pl.bristleback.server.bristle.api.annotations.Interceptor;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import java.util.Set;

@Interceptor(stages = ActionExecutionStage.PARAMETERS_EXTRACTION)
@Component
public class ValidatorActionInterceptor implements ActionInterceptor<ValidationInterceptorContext> {

  @Inject
  private ValidationInterceptorContextResolver validationInterceptorContextResolver;

  public ValidatorActionInterceptor() {
    System.out.println("ValidatorActionInterceptor");
  }

  @Override
  public void intercept(ActionExecutionContext context, ValidationInterceptorContext interceptorContext) {
    Set<ConstraintViolation<Object>> validationResults = interceptorContext.getValidator().validateParameters(context.getActionClassInstance(),
      context.getAction().getMethod(), context.getActionParameters());
    if (!validationResults.isEmpty()) {
      throw new ActionValidationException(validationResults);
    }
  }

  @Override
  public ActionInterceptorContextResolver<ValidationInterceptorContext> getContextResolver() {
    return validationInterceptorContextResolver;
  }
}
