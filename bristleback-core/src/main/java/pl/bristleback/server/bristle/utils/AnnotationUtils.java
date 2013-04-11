package pl.bristleback.server.bristle.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class containing methods related with annotations.
 * <p/>
 * Created on: 09.02.13 19:15 <br/>
 *
 * @author Wojciech Niemiec
 */
public final class AnnotationUtils {


  private AnnotationUtils() {
    throw new UnsupportedOperationException();
  }

  /**
   * Finds all occurrences of annotation of type specified in parameter, inside of {@link AnnotatedElement} implementation element.
   * This method searches also for occurrences that are  nested in other annotations,
   * but it doesn't use deep search, just one level.
   *
   * @param annotatedElement element in which annotation occurrences are searched.
   * @param annotationType   searched annotation class
   * @param <T>              type of annotation
   * @return list of annotation occurrences.
   *         While single annotated element can contain only occurrence of given annotation,
   *         it may contain other annotations that can be marked with that annotation.
   */
  public static <T extends Annotation> List<T> findNestedAnnotations(AnnotatedElement annotatedElement, Class<T> annotationType) {
    List<T> nestedAnnotations = new ArrayList<T>();
    if (annotatedElement.isAnnotationPresent(annotationType)) {
      nestedAnnotations.add(annotatedElement.getAnnotation(annotationType));
    }
    for (Annotation annotation : annotatedElement.getAnnotations()) {
      T searchedAnnotation = findAnnotation(annotation, annotationType);
      if (searchedAnnotation != null) {
        nestedAnnotations.add(searchedAnnotation);
      }
    }
    return nestedAnnotations;
  }

  /**
   * Finds annotation of type specified in parameter, inside of other annotation.
   * Returns null if parent annotation is not marked with such annotation type.
   *
   * @param parentAnnotation parent annotation
   * @param annotationType   type of annotation that will be searched inside of parent annotation.
   * @param <T>              searched annotation type
   * @return occurrence of searched annotation or null it no occurrence could be found.
   */
  @SuppressWarnings("unchecked")
  public static <T extends Annotation> T findAnnotation(Annotation parentAnnotation, Class<T> annotationType) {
    for (Annotation childAnnotation : parentAnnotation.annotationType().getAnnotations()) {
      if (childAnnotation.annotationType().equals(annotationType)) {
        return (T) childAnnotation;
      }
    }
    return null;
  }

}
