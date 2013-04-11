package pl.bristleback.server.bristle.message;

import pl.bristleback.server.bristle.api.WebsocketConnector;
import pl.bristleback.server.bristle.api.WebsocketMessage;

import java.util.List;

/**
 * //@todo class description
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