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

package pl.bristleback.server.bristle.conf.resolver.action.client;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.action.client.ClientActionClassInformation;
import pl.bristleback.server.bristle.action.client.ClientActionInformation;
import pl.bristleback.server.bristle.api.annotations.ClientAction;
import pl.bristleback.server.bristle.api.annotations.ClientActionClass;
import pl.bristleback.server.bristle.conf.BristlebackComponentsContainer;

import javax.inject.Inject;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-07-05 09:54:10 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component
public class ClientActionClassesResolver {

  @Inject
  private BristlebackComponentsContainer componentsContainer;

  @Inject
  private ClientActionResolver clientActionResolver;

  public Map<String, ClientActionClassInformation> resolve() {
    Map<String, ClientActionClassInformation> actionClasses = new HashMap<String, ClientActionClassInformation>();

    Map<String, Object> foundActions = componentsContainer.getActualContext().getBeansWithAnnotation(ClientActionClass.class);
    for (Map.Entry<String, Object> actionClassEntry : foundActions.entrySet()) {
      Object actionClass = actionClassEntry.getValue();
      ClientActionClassInformation actionClassInformation = prepareActionClass(actionClass);
      actionClasses.put(actionClassInformation.getName(), actionClassInformation);
    }

    return actionClasses;
  }

  private ClientActionClassInformation prepareActionClass(Object actionClassInstance) {
    Class actionClass = actionClassInstance.getClass().getSuperclass(); // skip proxy class
    ClientActionClass actionClassAnnotation = AnnotationUtils.findAnnotation(actionClass, ClientActionClass.class);
    String actionClassName = getActionClassName(actionClass, actionClassAnnotation);
    Map<String, ClientActionInformation> actions = prepareActions(actionClass, actionClassName);

    return new ClientActionClassInformation(actionClassName, actions);
  }

  private String getActionClassName(Class<?> actionClass, ClientActionClass actionClassAnnotation) {
    if (StringUtils.isNotBlank(actionClassAnnotation.name())) {
      return actionClassAnnotation.name();
    }
    return actionClass.getSimpleName();
  }

  private Map<String, ClientActionInformation> prepareActions(Class<?> actionClass, String actionClassName) {
    Map<String, ClientActionInformation> actionsMap = new HashMap<String, ClientActionInformation>();
    for (Method action : actionClass.getMethods()) {
      ClientAction actionClassAnnotation = AnnotationUtils.findAnnotation(action, ClientAction.class);
      if (actionClassAnnotation != null) {
        ClientActionInformation actionInformation = clientActionResolver.prepareActionInformation(actionClassName, action);
        actionsMap.put(actionInformation.getName(), actionInformation);
      }
    }
    return actionsMap;
  }
}
