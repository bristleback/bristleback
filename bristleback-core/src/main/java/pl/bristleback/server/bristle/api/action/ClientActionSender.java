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

import pl.bristleback.server.bristle.action.client.ClientActionInformation;
import pl.bristleback.common.serialization.message.BristleMessage;
import pl.bristleback.server.bristle.message.ConditionObjectSender;

/**
 * By default, client actions must return one of the fallowing object types:
 * <ol>
 * <li>{@link SendCondition} implementation</li>
 * <li>{@link pl.bristleback.server.bristle.api.users.UserContext} implementation</li>
 * <li>List of {@link pl.bristleback.server.bristle.api.users.UserContext} implementations</li>
 * </ol>
 * To extend this behaviour, you need to create a class implementing this interface.
 * <p/>
 * Created on: 2012-07-07 21:08:16 <br/>
 *
 * @author Wojciech Niemiec
 */
public interface ClientActionSender<T> {

  /**
   * Sends message to one or more clients using action condition given as parameter.
   * Type of action condition is the type that client action should return to be processed by this client action sender implementation.
   *
   * @param actionCondition   action condition used to determine recipients of client action message.
   * @param message           message to sent.
   * @param objectSender      object sender.
   * @param actionInformation information about client action that sent actual action condition.
   * @throws Exception
   */
  void sendClientAction(T actionCondition, BristleMessage message, ConditionObjectSender objectSender, ClientActionInformation actionInformation) throws Exception;
}
