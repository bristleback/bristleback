/*
 * Bristleback Websocket Framework - Copyright (c) 2010-2013 http://bristleback.pl
 * ---------------------------------------------------------------------------
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation; either version 3 of the License, or (at your
 * option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but without any warranty; without even the implied warranty of merchantability
 * or fitness for a particular purpose.
 * You should have received a copy of the GNU Lesser General Public License along
 * with this program; if not, see <http://www.gnu.org/licenses/lgpl.html>.
 * ---------------------------------------------------------------------------
 */

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
