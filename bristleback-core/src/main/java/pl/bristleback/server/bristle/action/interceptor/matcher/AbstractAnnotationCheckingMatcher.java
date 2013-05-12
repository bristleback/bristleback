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
