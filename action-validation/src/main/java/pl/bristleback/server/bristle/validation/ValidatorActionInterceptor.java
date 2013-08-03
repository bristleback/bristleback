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

/**
 * Validation interceptor uses JSR 303: Bean Validation 1.1 specification to validate action parameters.
 * By default, Hibernate Validator of version <strong>5.0.1.Final</strong> is used.
 * <p/>
 * Single {@link javax.validation.ValidatorFactory} instance is created using
 * {@link pl.bristleback.server.bristle.validation.init.ValidatorFactoryInitializer} implementation.
 * Customization of validator factory is possible by creating custom bean implementing
 * {@link pl.bristleback.server.bristle.validation.init.ValidatorFactoryInitializer} interface.
 * <p/>
 * In case of validation error, exception response message is sent to client,
 * with payload containing a list of {@link ActionConstraintViolation} objects.
 * <p/>
 * There is a known limitation in bean validation used in Bristleback and it's related with Bean Validation specification.
 * Default actions cannot be validated. More about the issue can be found, for example,
 * in <a href="http://docs.jboss.org/hibernate/stable/validator/reference/en-US/html_single/#d0e1860">
 * Hibernate Validator documentation - Method constraints in inheritance hierarchies
 * </a>
 *
 * @see <a href="http://beanvalidation.org/1.1/">Bean Validation 1.1 specification</a>
 */
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
