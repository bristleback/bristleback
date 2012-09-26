package pl.bristleback.server.bristle.message.akka;

import pl.bristleback.server.bristle.api.WebsocketConnector;
import pl.bristleback.server.bristle.api.WebsocketMessage;

/**
 * Wrapper binding message that will be set to single connector
 * created at 22.09.12
 *
 * @author Pawel Machowski
 */
public class MessageForConnector {

  private WebsocketMessage websocketMessage;
  private WebsocketConnector connector;

  public MessageForConnector(WebsocketMessage websocketMessage, WebsocketConnector connector) {
    this.websocketMessage = websocketMessage;
    this.connector = connector;
  }

  public WebsocketMessage getWebsocketMessage() {
    return websocketMessage;
  }

  public WebsocketConnector getConnector() {
    return connector;
  }
}
