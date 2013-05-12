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

import org.apache.commons.lang.StringUtils;
import pl.bristleback.server.bristle.action.exception.BrokenActionProtocolException;

import java.util.HashMap;
import java.util.Map;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-07-20 11:46:01 <br/>
 *
 * @author Wojciech Niemiec
 */
public class ActionClassInformation {

  private boolean singleton;

  private String name;

  private String springBeanName;

  private Object actionClassInstance;

  private Map<String, ActionInformation> actions;

  private ActionInformation defaultAction;

  private Class<?> type;

  public ActionClassInformation() {
    actions = new HashMap<String, ActionInformation>();
  }

  public boolean hasDefaultAction() {
    return defaultAction != null;
  }

  public ActionInformation getActionToExecute(ActionExecutionContext context) {
    String actionName = context.getActionName();
    if (StringUtils.isEmpty(actionName)) {
      if (!hasDefaultAction()) {
        throw new BrokenActionProtocolException(BrokenActionProtocolException.ReasonType.NO_DEFAULT_ACTION_FOUND, "Action class " + name + " does not have default action, specify action to execute");
      }
      return defaultAction;
    }

    ActionInformation actionToExecute = actions.get(actionName);
    if (actionToExecute == null) {
      throw new BrokenActionProtocolException(BrokenActionProtocolException.ReasonType.NO_ACTION_FOUND, "Action class \"" + name + "\" does not have action \"" + actionName + "\".");
    }
    if (actionToExecute.getParameters().size() > context.getMessage().getPayload().length) {
      throw new BrokenActionProtocolException(BrokenActionProtocolException.ReasonType.TOO_FEW_ACTION_PARAMETERS,
        "Action " + context.getMessage().getName() + " takes " + actionToExecute.getParameters().size() + " parameter(s),"
          + " found: " + context.getMessage().getPayload().length + ".");
    }
    return actionToExecute;
  }

  public boolean isSingleton() {
    return singleton;
  }

  public void setSingleton(boolean singleton) {
    this.singleton = singleton;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setDefaultAction(ActionInformation defaultAction) {
    this.defaultAction = defaultAction;
  }

  public Map<String, ActionInformation> getActions() {
    return actions;
  }

  public Object getSingletonActionClassInstance() {
    return actionClassInstance;
  }

  public void setSingletonActionClassInstance(Object singletonActionClassInstance) {
    this.actionClassInstance = singletonActionClassInstance;
  }

  public Class<?> getType() {
    return type;
  }

  public void setType(Class<?> type) {
    this.type = type;
  }

  public String getSpringBeanName() {
    return springBeanName;
  }

  public void setSpringBeanName(String springBeanName) {
    this.springBeanName = springBeanName;
  }
}
