package pl.bristleback.server.bristle.action.interceptor.matcher;

import pl.bristleback.server.bristle.action.ActionInformation;
import pl.bristleback.server.bristle.api.action.ActionInterceptor;
import pl.bristleback.server.bristle.api.action.ActionInterceptorMatcher;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Base class for annotation checking interceptor matcher classes.
 * <p/>
 * Created on: 14.02.13 21:40 <br/>
 *
 * @author Wojciech Niemiec
 */
public abstract class AbstractAnnotationCheckingMatcher implements ActionInterceptorMatcher {

  private List<Class<? extends Annotation>> annotationClasses = new ArrayList<Class<? extends Annotation>>();

  protected AbstractAnnotationCheckingMatcher() {
  }

  protected AbstractAnnotationCheckingMatcher(List<Class<? extends Annotation>> annotationClasses) {
    this.annotationClasses = annotationClasses;
  }

  protected AbstractAnnotationCheckingMatcher(Class<? extends Annotation> annotationClass) {
    this.annotationClasses.add(annotationClass);
  }

  protected abstract AnnotatedElement getElementToCheck(ActionInformation actionInformation);

  @Override
  public boolean isInterceptorApplicable(ActionInformation actionInformation, Class<? extends ActionInterceptor> interceptor) {
    AnnotatedElement annotatedElement = getElementToCheck(actionInformation);
    for (Class<? extends Annotation> annotationClass : annotationClasses) {
      if (!annotatedElement.isAnnotationPresent(annotationClass)) {
        return false;
      }
    }
    return true;
  }

  public void setAnnotationClasses(List<Class<? extends Annotation>> annotationClasses) {
    this.annotationClasses = annotationClasses;
  }

  public void setAnnotationClassesArray(Class<? extends Annotation>... annotationClassesArray) {
    this.annotationClasses = Arrays.asList(annotationClassesArray);
  }

}
