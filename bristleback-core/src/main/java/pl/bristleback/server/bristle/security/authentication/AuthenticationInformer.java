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

package pl.bristleback.server.bristle.security.authentication;

import org.apache.log4j.Logger;
import pl.bristleback.server.bristle.api.annotations.ObjectSender;
import pl.bristleback.server.bristle.api.users.UserContext;
import pl.bristleback.server.bristle.api.users.UserDetails;
import pl.bristleback.common.serialization.message.BristleMessage;
import pl.bristleback.server.bristle.message.ConditionObjectSender;

import java.util.Collections;

/**
 * This component is used to send messages about events related with authentication process.
 * <p/>
 * Created on: 23.03.13 12:20 <br/>
 *
 * @author Wojciech Niemiec
 */
public class AuthenticationInformer {

  private static final String AUTHENTICATION_SUCCESS_MESSAGE_NAME = "SystemAuth.authenticationSuccess";

  private static final String LOGOUT_MESSAGE_NAME = "SystemAuth.logout";

  private static Logger log = Logger.getLogger(AuthenticationInformer.class.getName());

  @ObjectSender
  private ConditionObjectSender objectSender;

  public void sendAuthenticationSuccessInformation(UserContext userContext, UserDetails userDetails) {
    BristleMessage<String> message = new BristleMessage<String>()
      .withName(AUTHENTICATION_SUCCESS_MESSAGE_NAME)
      .withPayload(userDetails.getUsername());

    sendMessage(message, userContext);
  }

  public void sendLogoutInformation(UserContext userContext, String username, LogoutReason logoutReason) {
    BristleMessage<LogoutMessagePayload> message = new BristleMessage<LogoutMessagePayload>()
      .withName(LOGOUT_MESSAGE_NAME)
      .withPayload(new LogoutMessagePayload(username, logoutReason));

    sendMessage(message, userContext);
  }

  private void sendMessage(BristleMessage<?> message, UserContext userContext) {
    try {
      objectSender.sendMessage(message, Collections.singletonList(userContext));
    } catch (Exception e) {
      log.error("Bristleback could not send authentication system message", e);
    }
  }
}
