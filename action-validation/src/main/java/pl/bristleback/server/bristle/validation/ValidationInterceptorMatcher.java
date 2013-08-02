package pl.bristleback.server.bristle.validation;


import pl.bristleback.server.bristle.action.ActionInformation;
import pl.bristleback.server.bristle.api.action.ActionInterceptor;
import pl.bristleback.server.bristle.api.action.ActionInterceptorMatcher;
import pl.bristleback.server.bristle.utils.AnnotationUtils;

import javax.validation.Constraint;
import javax.validation.Valid;
import java.lang.annotation.Annotation;

public class ValidationInterceptorMatcher implements ActionInterceptorMatcher {

  @Override
  public boolean isInterceptorApplicable(ActionInformation actionInformation, Class<? extends ActionInterceptor> interceptor) {
    boolean parameterValidationExist = isParameterValidationExist(actionInformation);
    boolean crossParameterValidationExist = isCrossParameterValidationExist(actionInformation);
    return parameterValidationExist || crossParameterValidationExist;
  }

  private boolean isParameterValidationExist(ActionInformation actionInformation) {
    for (Annotation[] parameterAnnotations : actionInformation.getMethod().getParameterAnnotations()) {
      boolean containsValidationAnnotation = isAnnotationPresent(parameterAnnotations, Valid.class);
      boolean containsConstraintAnnotation = isAnnotationPresent(parameterAnnotations, Constraint.class);
      if (containsValidationAnnotation || containsConstraintAnnotation) {
        return true;
      }
    }
    return false;
  }

  private boolean isCrossParameterValidationExist(ActionInformation actionInformation) {
    return isAnnotationPresent(actionInformation.getMethod().getAnnotations(), Constraint.class);
  }

  private boolean isAnnotationPresent(Annotation[] parameterAnnotations, Class<? extends Annotation> annotationToFind) {
    for (Annotation parameterAnnotation : parameterAnnotations) {
      if (parameterAnnotation.annotationType().equals(annotationToFind) || AnnotationUtils.findAnnotation(parameterAnnotation, annotationToFind) != null) {
        return true;
      }
    }
    return false;
  }
}
