package pl.bristleback.server.bristle.action.interceptor.matcher;

import pl.bristleback.server.bristle.action.ActionInformation;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.List;

/**
 * This matcher checks whether given action class contains <strong>ALL</strong> required annotations.
 * <p/>
 * Created on: 14.02.13 21:35 <br/>
 *
 * @author Wojciech Niemiec
 */
public class AnnotationCheckingActionClassMatcher extends AbstractAnnotationCheckingMatcher {

  public AnnotationCheckingActionClassMatcher() {
  }

  public AnnotationCheckingActionClassMatcher(List<Class<? extends Annotation>> annotationClasses) {
    super(annotationClasses);
  }

  public AnnotationCheckingActionClassMatcher(Class<? extends Annotation> annotationClass) {
    super(annotationClass);
  }

  @Override
  protected AnnotatedElement getElementToCheck(ActionInformation actionInformation) {
    return actionInformation.getActionClass().getType();
  }
}
