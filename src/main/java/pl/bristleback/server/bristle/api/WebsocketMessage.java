package pl.bristleback.server.bristle.api;

import pl.bristleback.server.bristle.message.MessageType;

import java.util.List;

/**
 * Low level WebSockets message used by a Server Engine. Should not be created by application user.
 * Contains payload and list of recipients.
 * <p/>
 * Created on: 2011-07-19 13:17:51 <br/>
 *
 * @author Wojciech Niemiec
 */
public interface WebsocketMessage<T> {

  MessageType getMessageType();

  List<WebsocketConnector> getRecipients();

  void setRecipients(List<WebsocketConnector> recipients);

  T getContent();

  void setContent(T content);
} 
