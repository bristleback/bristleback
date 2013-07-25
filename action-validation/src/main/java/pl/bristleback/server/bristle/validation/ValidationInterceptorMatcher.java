package pl.bristleback.server.bristle.validation;


import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.action.ActionInformation;
import pl.bristleback.server.bristle.api.action.ActionInterceptor;
import pl.bristleback.server.bristle.api.action.ActionInterceptorMatcher;
import pl.bristleback.server.bristle.utils.AnnotationUtils;

import javax.validation.Valid;
import java.lang.annotation.Annotation;

@Component
public class ValidationInterceptorMatcher implements ActionInterceptorMatcher {

  @Override
  public boolean isInterceptorApplicable(ActionInformation actionInformation, Class<? extends ActionInterceptor> interceptor) {
    for (Annotation[] parameterAnnotations : actionInformation.getMethod().getParameterAnnotations()) {
      boolean containsValidationAnnotation = isValidAnnotationPresent(parameterAnnotations);
      if (containsValidationAnnotation) {
        return true;
      }
    }
    return false;
  }

  private boolean isValidAnnotationPresent(Annotation[] parameterAnnotations) {
    for (Annotation parameterAnnotation : parameterAnnotations) {
      if (parameterAnnotation.annotationType().equals(Valid.class) || AnnotationUtils.findAnnotation(parameterAnnotation, Valid.class) != null) {
        return true;
      }
    }
    return false;
  }
}
