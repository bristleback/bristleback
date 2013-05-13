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

package pl.bristleback.server.bristle.conf.resolver.action.interceptor;

import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.action.ActionClassInformation;
import pl.bristleback.server.bristle.action.ActionInformation;
import pl.bristleback.server.bristle.action.interceptor.ActionInterceptorInformation;
import pl.bristleback.server.bristle.action.interceptor.ActionInterceptorMapping;
import pl.bristleback.server.bristle.action.interceptor.ActionInterceptors;
import pl.bristleback.server.bristle.action.interceptor.InterceptionProcessContext;
import pl.bristleback.server.bristle.api.action.ActionInterceptor;
import pl.bristleback.server.bristle.api.annotations.Intercept;
import pl.bristleback.server.bristle.api.annotations.Interceptor;
import pl.bristleback.server.bristle.conf.BristleInitializationException;
import pl.bristleback.server.bristle.integration.spring.BristleSpringIntegration;
import pl.bristleback.server.bristle.utils.AnnotationUtils;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is used for finding and preparing interceptions for each action class.
 * It also contains cached information about all interceptor components,
 * stored in {@link ActionInterceptorInformation} objects.
 * <p/>
 * Created on: 24.01.13 17:35 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component
public class ActionInterceptorsResolver {

  @Inject
  private BristleSpringIntegration springIntegration;

  @Inject
  private ActionInterceptorsSorter actionInterceptorsSorter;

  @Inject
  private ActionInterceptorContextsResolver actionInterceptorContextsResolver;

  private Map<Class, ActionInterceptorInformation> allInterceptors;

  private List<ActionInterceptorMapping> allInterceptorMappings;

  @PostConstruct
  private void initInterceptorResolver() {
    loadAllInterceptors();
    loadInterceptorMatcherList();
  }

  private void loadInterceptorMatcherList() {
    allInterceptorMappings =
      new ArrayList<ActionInterceptorMapping>(springIntegration.getBeansOfType(ActionInterceptorMapping.class).values());
  }

  private void loadAllInterceptors() {
    allInterceptors = new HashMap<Class, ActionInterceptorInformation>();
    Map<String, ActionInterceptor> interceptorBeans = springIntegration.getBeansOfType(ActionInterceptor.class);
    for (ActionInterceptor actionInterceptor : interceptorBeans.values()) {
      ActionInterceptorInformation interceptorInformation = loadInterceptor(actionInterceptor);
      if (allInterceptors.containsKey(actionInterceptor.getClass())) {
        throw new BristleInitializationException("Currently, it is not possible to define more than one "
          + "action interceptor bean of the same type.");
      }
      allInterceptors.put(actionInterceptor.getClass(), interceptorInformation);
    }
  }

  private ActionInterceptorInformation loadInterceptor(ActionInterceptor actionInterceptor) {
    Class<?> interceptorClass = actionInterceptor.getClass();
    if (!interceptorClass.isAnnotationPresent(Interceptor.class)) {
      throw new BristleInitializationException("Interceptor classes must be annotated with " + Interceptor.class);
    }
    Interceptor interceptorAnnotation = interceptorClass.getAnnotation(Interceptor.class);
    return new ActionInterceptorInformation(actionInterceptor, actionInterceptor.getContextResolver(), interceptorAnnotation.stages());
  }

  public void resolveInterceptors(ActionClassInformation actionClassInformation) {
    List<ActionInterceptorInformation> interceptorsInformationForClass = getInterceptorDescriptionsForClass(actionClassInformation);
    for (ActionInformation actionInformation : actionClassInformation.getActions().values()) {
      List<Class<? extends ActionInterceptor>> interceptorClasses = getInterceptorsUsed(actionInformation);
      List<ActionInterceptorInformation> interceptorsInformationForAction = resolveInterceptorsInformation(interceptorClasses);
      interceptorsInformationForAction.addAll(interceptorsInformationForClass);
      List<InterceptionProcessContext> interceptorContexts = actionInterceptorContextsResolver.resolveContexts(interceptorsInformationForAction, actionInformation);
      ActionInterceptors sortedInterceptors = actionInterceptorsSorter.sortInterceptors(interceptorContexts);
      actionInformation.setActionInterceptors(sortedInterceptors);
    }
  }

  private List<Class<? extends ActionInterceptor>> getInterceptorsUsed(ActionInformation actionInformation) {
    List<Class<? extends ActionInterceptor>> interceptorClasses = getInterceptorsUsed(actionInformation.getMethod());

    for (ActionInterceptorMapping actionInterceptorMapping : allInterceptorMappings) {
      boolean interceptorApplicable = actionInterceptorMapping.getInterceptorMatcher()
        .isInterceptorApplicable(actionInformation, actionInterceptorMapping.getInterceptorClass());
      if (interceptorApplicable) {
        interceptorClasses.add(actionInterceptorMapping.getInterceptorClass());
      }
    }

    return interceptorClasses;
  }

  private List<Class<? extends ActionInterceptor>> getInterceptorsUsed(AnnotatedElement annotatedElement) {
    List<Class<? extends ActionInterceptor>> interceptorClasses = new ArrayList<Class<? extends ActionInterceptor>>();
    List<Intercept> interceptAnnotations = AnnotationUtils.findNestedAnnotations(annotatedElement, Intercept.class);
    for (Intercept interceptAnnotation : interceptAnnotations) {
      interceptorClasses.addAll(getInterceptorBeans(interceptAnnotation));
    }
    return interceptorClasses;
  }

  private List<Class<? extends ActionInterceptor>> getInterceptorBeans(Intercept interceptAnnotation) {
    List<Class<? extends ActionInterceptor>> interceptors = new ArrayList<Class<? extends ActionInterceptor>>();
    for (String interceptorReference : interceptAnnotation.refs()) {
      interceptors.add(springIntegration.getBean(interceptorReference, ActionInterceptor.class).getClass());
    }
    for (Class<? extends ActionInterceptor> interceptorClass : interceptAnnotation.value()) {
      interceptors.add(springIntegration.getBean(interceptorClass).getClass());
    }
    return interceptors;
  }

  private List<ActionInterceptorInformation> getInterceptorDescriptionsForClass(ActionClassInformation actionClassInformation) {
    List<Class<? extends ActionInterceptor>> interceptorClasses = getInterceptorsUsed(actionClassInformation.getType());
    return resolveInterceptorsInformation(interceptorClasses);
  }

  private List<ActionInterceptorInformation> resolveInterceptorsInformation(List<Class<? extends ActionInterceptor>>
                                                                              interceptorClasses) {
    List<ActionInterceptorInformation> interceptors = new ArrayList<ActionInterceptorInformation>();
    for (Class<? extends ActionInterceptor> interceptorClass : interceptorClasses) {
      if (!allInterceptors.containsKey(interceptorClass)) {
        throw new BristleInitializationException("Cannot find interceptor Spring bean of type: " + interceptorClass);
      }
      interceptors.add(allInterceptors.get(interceptorClass));
    }
    return interceptors;
  }

}
