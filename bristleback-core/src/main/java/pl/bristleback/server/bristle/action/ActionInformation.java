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

package pl.bristleback.server.bristle.action;

import org.apache.commons.collections.CollectionUtils;
import pl.bristleback.server.bristle.action.interceptor.ActionInterceptors;
import pl.bristleback.server.bristle.action.response.ActionResponseInformation;
import pl.bristleback.server.bristle.api.action.DefaultAction;
import pl.bristleback.server.bristle.api.users.UserContext;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-07-20 17:56:33 <br/>
 *
 * @author Wojciech Niemiec
 */
public class ActionInformation {

  private static final int CONNECTOR_ACTION_PARAMETER = 0;

  private static final int SECOND_ACTION_PARAMETER = 1;

  private String name;

  private boolean defaultAction;

  private ActionClassInformation actionClass;

  private Method method;

  private List<ActionParameterInformation> parameters;

  private ActionResponseInformation responseInformation;

  private ActionInterceptors actionInterceptors;

  public ActionInformation(String name, Method method, boolean defaultAction) {
    this.name = name;
    this.method = method;
    this.defaultAction = defaultAction;
  }

  @SuppressWarnings("unchecked")
  public Object execute(Object actionInstance, Object[] parameterValues) throws Exception {
    if (defaultAction) {
      return ((DefaultAction) actionInstance)
        .executeDefault((UserContext) parameterValues[CONNECTOR_ACTION_PARAMETER],
          parameterValues[SECOND_ACTION_PARAMETER]);
    }
    return method.invoke(actionInstance, parameterValues);
  }

  public boolean isDefaultAction() {
    return defaultAction;
  }

  public String getName() {
    return name;
  }

  public List<ActionParameterInformation> getParameters() {
    return parameters;
  }

  public void setParameters(List<ActionParameterInformation> parameters) {
    this.parameters = parameters;
  }

  @Override
  public String toString() {
    StringBuilder paramsToString = new StringBuilder();
    if (CollectionUtils.isNotEmpty(parameters)) {
      Iterator<ActionParameterInformation> it = parameters.iterator();
      while (it.hasNext()) {
        paramsToString.append(it.next().toString());
        if (it.hasNext()) {
          paramsToString.append(", ");
        }
      }
    }

    return "action: " + name + "(" + paramsToString.toString() + ")";
  }

  public ActionResponseInformation getResponseInformation() {
    return responseInformation;
  }

  public void setResponseInformation(ActionResponseInformation responseInformation) {
    this.responseInformation = responseInformation;
  }

  public ActionInterceptors getActionInterceptors() {
    return actionInterceptors;
  }

  public void setActionInterceptors(ActionInterceptors actionInterceptors) {
    this.actionInterceptors = actionInterceptors;
  }

  public ActionClassInformation getActionClass() {
    return actionClass;
  }

  public void setActionClass(ActionClassInformation actionClassInformation) {
    this.actionClass = actionClassInformation;
  }

  public Method getMethod() {
    return method;
  }
}