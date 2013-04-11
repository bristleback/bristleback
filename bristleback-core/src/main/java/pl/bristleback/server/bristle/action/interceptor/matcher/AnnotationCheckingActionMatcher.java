package pl.bristleback.server.bristle.action.interceptor.matcher;

import pl.bristleback.server.bristle.action.ActionInformation;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.List;

/**
 * This matcher checks whether given action contains <strong>ALL</strong> required annotations.
 * <p/>
 * Created on: 14.02.13 21:44 <br/>
 *
 * @author Wojciech Niemiec
 */
public class AnnotationCheckingActionMatcher extends AbstractAnnotationCheckingMatcher {

  public AnnotationCheckingActionMatcher() {
  }

  public AnnotationCheckingActionMatcher(List<Class<? extends Annotation>> annotationClasses) {
    super(annotationClasses);
  }

  public AnnotationCheckingActionMatcher(Class<? extends Annotation> annotationClass) {
    super(annotationClass);
  }

  @Override
  protected AnnotatedElement getElementToCheck(ActionInformation actionInformation) {
    return actionInformation.getMethod();
  }
}
