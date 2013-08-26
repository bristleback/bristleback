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

import pl.bristleback.common.serialization.message.BristleMessage;
import pl.bristleback.server.bristle.utils.StringUtils;

/**
 * This factory class contains methods used to create various types of server action messages.
 * <p/>
 * Created on: 2012-08-18 08:48:38 <br/>
 *
 * @author Wojciech Niemiec
 * @see BristleMessage
 */
public final class ActionMessageFactory {

  private ActionMessageFactory() {
    throw new UnsupportedOperationException();
  }

  /**
   * Creates a default action message with action class name and payload defined,
   * that can be then sent using {@link pl.bristleback.server.bristle.message.ConditionObjectSender}.
   *
   * @param actionClass action class name.
   * @param payload     message payload.
   * @param <T>         payload type.
   * @return server action message.
   */
  public static <T> BristleMessage<T> createDefaultMessage(String actionClass, T payload) {
    BristleMessage<T> message = new BristleMessage<T>();
    message.withName(actionClass).withPayload(payload);
    return message;
  }

  /**
   * Creates an action message with action name and payload defined,
   * that can be then sent using {@link pl.bristleback.server.bristle.message.ConditionObjectSender}.
   *
   * @param actionClass action class name.
   * @param action      ation name.
   * @param payload     message payload.
   * @param <T>         payload type.
   * @return server action message.
   */
  public static <T> BristleMessage<T> createMessage(String actionClass, String action, T payload) {
    BristleMessage<T> message = new BristleMessage<T>();
    message.withName(actionClass + StringUtils.DOT_AS_STRING + action).withPayload(payload);
    return message;
  }
}
