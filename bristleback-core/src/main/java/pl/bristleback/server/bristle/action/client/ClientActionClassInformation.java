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

package pl.bristleback.server.bristle.action.client;

import pl.bristleback.server.bristle.conf.resolver.action.ActionResolvingUtils;
import pl.bristleback.server.bristle.action.exception.ClientActionException;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-05-26 20:13:14 <br/>
 *
 * @author Wojciech Niemiec
 */
public class ClientActionClassInformation {

  private static final boolean DO_NOT_VALIDATE_ACTION_NAME = false;

  private String name;

  private Map<String, ClientActionInformation> clientActions;

  public ClientActionClassInformation(String name, Map<String, ClientActionInformation> clientActions) {
    this.name = name;
    this.clientActions = clientActions;
  }

  public ClientActionInformation getClientAction(String actionName) {
    ClientActionInformation actionInformation = clientActions.get(actionName);
    if (actionInformation == null) {
      throw new ClientActionException("Action with name " + actionName + " cannot be found");
    }
    return actionInformation;
  }

  public String getName() {
    return name;
  }

  public ClientActionInformation getClientAction(Method action) {
    String actionName = ActionResolvingUtils.resolveClientActionName(action, DO_NOT_VALIDATE_ACTION_NAME);
    return getClientAction(actionName);
  }

  public Map<String, ClientActionInformation> getClientActions() {
    return clientActions;
  }
}
