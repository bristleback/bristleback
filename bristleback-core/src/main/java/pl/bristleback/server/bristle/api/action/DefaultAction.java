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

package pl.bristleback.server.bristle.api.action;


import pl.bristleback.server.bristle.api.users.UserContext;

/**
 * Classes implementing this interface contain default actions ready to be executed by clients.
 * This interface should be implemented by action classes (classes marked with {@link pl.bristleback.server.bristle.api.annotations.ActionClass} annotation).
 * This is optional interface, default actions are not required in every action classes.
 * However, they might be faster because reflection is not used to invoke them.
 * Default actions are way less flexible than normal actions, they always take two parameters:
 * current user context and a payload of the incoming message.  <br/>
 * <strong>As every action, default action method must be annotated with {@link pl.bristleback.server.bristle.api.annotations.Action Action}.</strong> <br/>
 * Default action method may return any type (but return type must be specified in method signature),
 * set return type to {@link Void} if no response should be sent to client.
 * <p/>
 * Created on: 2011-07-20 12:29:18 <br/>
 *
 * @author Wojciech Niemiec
 */
public interface DefaultAction<U extends UserContext, T> {

  /**
   * Executes default action of implementing action class.
   *
   * @param userContext current user context implementation.
   * @param payload     custom type payload object.
   * @return response.
   * @throws Exception if any exception while action invocation occurs.
   *                   All exceptions are handled by the Action Controller.
   */
  Object executeDefault(U userContext, T payload) throws Exception;
} 
