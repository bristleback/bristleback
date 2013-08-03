package pl.bristleback.server.bristle.validation;


import pl.bristleback.server.bristle.action.ActionInformation;
import pl.bristleback.server.bristle.api.action.ActionInterceptor;
import pl.bristleback.server.bristle.api.action.ActionInterceptorMatcher;
import pl.bristleback.server.bristle.utils.AnnotationUtils;

import javax.validation.Constraint;
import javax.validation.Valid;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Validation interceptor matcher looks for methods with validation defined on parameters or on method level.
 * Only most nested declarations are searched.
 * For example, if given action class implements some interface with method that becomes Bristleback action,
 * only annotations from interface method declaration are searched.
 * This is because of Bean Validation specification.
 *
 * @see <a href="http://docs.jboss.org/hibernate/stable/validator/reference/en-US/html_single/#d0e1860">
 *      Method constraints in inheritance hierarchies</a>
 */
public class ValidationInterceptorMatcher implements ActionInterceptorMatcher {

  @Override
  public boolean isInterceptorApplicable(ActionInformation actionInformation, Class<? extends ActionInterceptor> interceptor) {
    Method topMostDeclaration = findTopMostDeclaration(actionInformation.getMethod());
    boolean parameterValidationExist = isParameterValidationExist(topMostDeclaration);
    boolean crossParameterValidationExist = isCrossParameterValidationExist(topMostDeclaration);
    return parameterValidationExist || crossParameterValidationExist;
  }

  private Method findTopMostDeclaration(Method method) {
    String name = method.getName();
    Class<?>[] parameters = method.getParameterTypes();

    Method topMostDeclaration = findTopMostMethodInTree(method.getDeclaringClass(), name, parameters);
    Class<?> topMostOwner = topMostDeclaration.getDeclaringClass();

    Class<?>[] topMostClassInterfaces = topMostOwner.getInterfaces();
    for (Class<?> topMostClassInterface : topMostClassInterfaces) {
      Method foundInInterface = findTopMostMethodInTree(topMostClassInterface, name, parameters);
      if (foundInInterface != null) {
        topMostDeclaration = foundInInterface;
      }
    }
    return topMostDeclaration;
  }

  private Method findTopMostMethodInTree(Class<?> ownerClass, String methodName, Class<?>[] parameterTypes) {
    boolean processingFinished = false;
    Method topMostDeclaration = null;
    Class<?> actualClass = ownerClass;

    while (!processingFinished) {
      if (actualClass == null || actualClass.equals(Object.class)) {
        processingFinished = true;
      } else {
        boolean methodFoundInClass = false;
        for (Method method : actualClass.getMethods()) {
          if (method.getName().equals(methodName) && method.getParameterTypes().length == parameterTypes.length) {
            topMostDeclaration = method;
            methodFoundInClass = true;
          }
        }
        if (!methodFoundInClass) {
          processingFinished = true;
        }
        actualClass = actualClass.getSuperclass();
      }
    }
    return topMostDeclaration;
  }

  private boolean isParameterValidationExist(Method topMostDeclaration) {
    for (Annotation[] parameterAnnotations : topMostDeclaration.getParameterAnnotations()) {
      boolean containsValidationAnnotation = isAnnotationPresent(parameterAnnotations, Valid.class);
      boolean containsConstraintAnnotation = isAnnotationPresent(parameterAnnotations, Constraint.class);
      if (containsValidationAnnotation || containsConstraintAnnotation) {
        return true;
      }
    }
    return false;
  }

  private boolean isCrossParameterValidationExist(Method topMostDeclaration) {
    return isAnnotationPresent(topMostDeclaration.getAnnotations(), Constraint.class);
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
