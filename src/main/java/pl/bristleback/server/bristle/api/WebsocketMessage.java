package pl.bristleback.server.bristle.api;

import pl.bristleback.server.bristle.serialization.MessageType;

import java.util.List;

/**
 * //@todo class description
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
