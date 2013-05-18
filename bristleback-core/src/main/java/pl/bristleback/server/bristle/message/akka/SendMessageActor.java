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

package pl.bristleback.server.bristle.message.akka;

import akka.actor.UntypedActor;
import org.apache.log4j.Logger;
import pl.bristleback.server.bristle.api.ServerEngine;
import pl.bristleback.server.bristle.api.WebsocketMessage;
import pl.bristleback.server.bristle.message.MessageType;

/**
 * Actor sending message to single connector.
 * created at 16.09.12
 *
 * @author Pawel Machowski
 */
public class SendMessageActor extends UntypedActor {
  private static Logger log = Logger.getLogger(SendMessageActor.class.getName());

  private ServerEngine server;

  public SendMessageActor(ServerEngine serverEngine) {
    this.server = serverEngine;
  }

  // message handler
  public void onReceive(Object message) throws Exception {
    MessageForConnector messageForConnector = (MessageForConnector) message;
    WebsocketMessage websocketMessage = messageForConnector.getWebsocketMessage();
    if (websocketMessage.getMessageType() == MessageType.TEXT) {
      server.sendMessage(messageForConnector.getConnector(), (String) websocketMessage.getContent());
    } else if (websocketMessage.getMessageType() == MessageType.BINARY) {
      server.sendMessage(messageForConnector.getConnector(), (byte[]) websocketMessage.getContent());
    } else {
      log.debug("Cannot send a websocketMessage, unknown type of websocketMessage " + websocketMessage.getMessageType());
    }
  }

}
