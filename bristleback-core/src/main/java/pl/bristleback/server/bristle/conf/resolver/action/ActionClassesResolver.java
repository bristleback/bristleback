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

package pl.bristleback.server.bristle.conf.resolver.action;

import org.apache.commons.lang.StringUtils;
import org.springframework.aop.TargetClassAware;
import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.action.ActionClassInformation;
import pl.bristleback.server.bristle.action.ActionInformation;
import pl.bristleback.server.bristle.action.ActionsContainer;
import pl.bristleback.server.bristle.action.exception.ActionInitializationException;
import pl.bristleback.server.bristle.api.annotations.Action;
import pl.bristleback.server.bristle.api.annotations.ActionClass;
import pl.bristleback.server.bristle.conf.resolver.action.interceptor.ActionInterceptorsResolver;
import pl.bristleback.server.bristle.integration.spring.BristleSpringIntegration;
import pl.bristleback.server.bristle.utils.PropertyUtils;

import javax.inject.Inject;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-07-22 09:54:10 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component
public class ActionClassesResolver {

  @Inject
  private ActionResolver actionResolver;

  @Inject
  private BristleSpringIntegration springIntegration;

  @Inject
  private ActionInterceptorsResolver actionInterceptorsResolver;

  public  Map<String, ActionClassInformation> resolve() {
    Map<String, ActionClassInformation> actionClasses = new HashMap<String, ActionClassInformation>();
    Map<String, Object> foundActions = springIntegration.getBeansWithAnnotation(ActionClass.class);
    for (Map.Entry<String, Object> actionClassEntry : foundActions.entrySet()) {
      String actionClassBeanName = actionClassEntry.getKey();
      Object actionClass = actionClassEntry.getValue();
      ActionClassInformation actionClassInformation = prepareActionClassInformation(actionClass, actionClassBeanName);
      actionClasses.put(actionClassInformation.getName(), actionClassInformation);
      actionInterceptorsResolver.resolveInterceptors(actionClassInformation);
    }
      return actionClasses;
  }

  private ActionClassInformation prepareActionClassInformation(Object actionClass, String actionClassBeanName) {
    Class<?> actionClassType = getActionClassType(actionClass);
    ActionClassInformation actionClassInformation = prepareActionClass(actionClass, actionClassType, actionClassBeanName);
    prepareActions(actionClassType, actionClassInformation);
    return actionClassInformation;
  }

  private ActionClassInformation prepareActionClass(Object actionClass, Class<?> actionClassType, String actionClassBeanName) {
    ActionClassInformation actionClassInformation = new ActionClassInformation();

    ActionClass actionClassAnnotation = actionClassType.getAnnotation(ActionClass.class);
    String actionClassName = resolveActionClassName(actionClassType, actionClassAnnotation);
    actionClassInformation.setName(actionClassName);
    actionClassInformation.setSpringBeanName(actionClassBeanName);
    actionClassInformation.setType(actionClassType);
    actionClassInformation.setSingleton(springIntegration.isSingleton(actionClassBeanName));

    if (actionClassInformation.isSingleton()) {
      actionClassInformation.setSingletonActionClassInstance(actionClass);
    }
    return actionClassInformation;
  }

  private Class<?> getActionClassType(Object actionClass) {
    if (actionClass instanceof TargetClassAware) {
      return ((TargetClassAware) actionClass).getTargetClass();
    }
    return actionClass.getClass();
  }

  private String resolveActionClassName(Class<?> actionClassType, ActionClass actionClassAnnotation) {
    String actionClassName = actionClassAnnotation.name();
    if (StringUtils.isBlank(actionClassName)) {
      actionClassName = actionClassType.getSimpleName();
    }
    return actionClassName;
  }

  private void prepareActions(Class<?> actionClassType, ActionClassInformation actionClassInformation) {
    List<Method> actions = PropertyUtils.getMethodsAnnotatedWith(actionClassType, Action.class, true);
    for (Method action : actions) {
      ActionInformation actionInformation = actionResolver.prepareActionInformation(actionClassType, action);
      addCreatedAction(actionClassInformation, actionInformation);
    }
  }

  private void addCreatedAction(ActionClassInformation actionClassInformation, ActionInformation actionInformation) {
    actionInformation.setActionClass(actionClassInformation);
    if (actionInformation.isDefaultAction()) {
      actionClassInformation.setDefaultAction(actionInformation);
    }
    if (actionClassInformation.getActions().containsKey(actionInformation.getName())) {
      throw new ActionInitializationException("Currently, multiple methods with the same name in one action class are not allowed");
    }
    actionClassInformation.getActions().put(actionInformation.getName(), actionInformation);
  }


}