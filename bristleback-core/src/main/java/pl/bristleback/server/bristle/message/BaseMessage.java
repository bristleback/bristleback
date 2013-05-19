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

package pl.bristleback.server.bristle.message;

import pl.bristleback.server.bristle.api.WebsocketConnector;
import pl.bristleback.server.bristle.api.WebsocketMessage;

import java.util.List;

/**
 * Traditional Websocket message containing type of message, content and list of recipients.
 * <p/>
 * Created on: 2011-07-19 14:51:07 <br/>
 *
 * @author Wojciech Niemiec
 */
public class BaseMessage<T> implements WebsocketMessage<T> {

  private List<WebsocketConnector> recipients;

  private MessageType messageType;

  private T content;

  public BaseMessage(MessageType messageType) {
    this.messageType = messageType;
  }

  public List<WebsocketConnector> getRecipients() {
    return recipients;
  }

  public void setRecipients(List<WebsocketConnector> recipients) {
    this.recipients = recipients;
  }

  @Override
  public MessageType getMessageType() {
    return messageType;
  }

  @Override
  public T getContent() {
    return content;
  }

  @Override
  public void setContent(T content) {
    this.content = content;
  }
}