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

package pl.bristleback.server.bristle.action.client.strategy;

import org.springframework.stereotype.Component;
import pl.bristleback.common.serialization.message.BristleMessage;
import pl.bristleback.server.bristle.action.client.ClientActionInformation;
import pl.bristleback.server.bristle.api.action.ClientActionSender;
import pl.bristleback.server.bristle.api.users.UserContext;
import pl.bristleback.server.bristle.message.ConditionObjectSender;
import pl.bristleback.server.bristle.security.UsersContainer;

import javax.inject.Inject;
import java.util.Collections;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-07-08 14:08:09 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component
public class SingleUserSenderStrategy implements ClientActionSender<UserContext> {

  @Inject
  private UsersContainer connectedUsers;

  @Override
  public void sendClientAction(UserContext user, BristleMessage message, ConditionObjectSender objectSender, ClientActionInformation actionInformation) throws Exception {
    objectSender.sendMessage(message, actionInformation.getSerialization(), connectedUsers.getConnectorsByUsers(Collections.singletonList(user)));
  }
}
