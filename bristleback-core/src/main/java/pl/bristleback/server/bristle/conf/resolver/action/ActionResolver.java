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

import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.action.ActionInformation;
import pl.bristleback.server.bristle.action.ActionParameterInformation;
import pl.bristleback.server.bristle.action.response.ActionResponseInformation;
import pl.bristleback.server.bristle.api.action.DefaultAction;

import javax.inject.Inject;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-07-23 12:56:33 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component
public class ActionResolver {

  @Inject
  private ParameterResolver parametersResolver;

  @Inject
  private ResponseResolver responseResolver;

  public ActionInformation prepareActionInformation(Class<?> clazz, Method action) {
    ActionInformation actionInformation = prepareAction(clazz, action);

    List<ActionParameterInformation> parameters = new ArrayList<ActionParameterInformation>();
    for (int i = 0; i < action.getParameterTypes().length; i++) {
      Type parameterType = action.getGenericParameterTypes()[i];
      ActionParameterInformation parameterInformation =
        parametersResolver.prepareActionParameter(parameterType, action.getParameterAnnotations()[i]);
      parameters.add(parameterInformation);
    }
    actionInformation.setParameters(parameters);
    ActionResponseInformation responseInformation = responseResolver.resolveResponse(action);
    actionInformation.setResponseInformation(responseInformation);
    return actionInformation;
  }

  private ActionInformation prepareAction(Class<?> clazz, Method action) {
    String actionName = ActionResolvingUtils.resolveActionName(action);
    boolean defaultAction = isDefaultRemoteAction(clazz, action);

    ActionInformation actionInformation = new ActionInformation(actionName, action, defaultAction);

    return actionInformation;
  }

  private boolean isDefaultRemoteAction(Class clazz, Method action) {
    if (!DefaultAction.class.isAssignableFrom(clazz)) {
      // this action class does not have default action
      return false;
    }
    Method defaultMethod = DefaultAction.class.getMethods()[0];
    if (!defaultMethod.getName().equals(action.getName())) {
      return false;
    }
    Class[] defaultParameterTypes = defaultMethod.getParameterTypes();
    if (defaultParameterTypes.length != action.getParameterTypes().length) {
      return false;
    }
    int parametersLength = defaultParameterTypes.length;
    for (int i = 0; i < parametersLength - 1; i++) {
      Class defaultParameterType = defaultParameterTypes[i];
      if (!defaultParameterType.isAssignableFrom(action.getParameterTypes()[i])) {
        return false;
      }
    }
    Type requiredLastParameterType = action.getGenericParameterTypes()[parametersLength - 1];
    Type actualLastParameterType = null;
    for (int i = 0; i < clazz.getInterfaces().length; i++) {
      Class interfaceOfClass = clazz.getInterfaces()[i];
      if (DefaultAction.class.equals(interfaceOfClass)) {
        Type genericInterface = clazz.getGenericInterfaces()[i];
        if (genericInterface instanceof ParameterizedType) {
          actualLastParameterType = ((ParameterizedType) (genericInterface)).getActualTypeArguments()[1];
        } else {
          actualLastParameterType = Object.class;
        }
      }
    }

    return requiredLastParameterType.equals(actualLastParameterType);
  }
}