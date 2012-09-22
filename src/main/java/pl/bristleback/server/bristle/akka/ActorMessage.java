package pl.bristleback.server.bristle.akka;

import pl.bristleback.server.bristle.api.WebsocketConnector;
import pl.bristleback.server.bristle.api.WebsocketMessage;

/**
 * created at 22.09.12
 *
 * @author Pawel Machowski
 */
public class ActorMessage {

  private WebsocketMessage websocketMessage;
  private WebsocketConnector connector;

  public ActorMessage(WebsocketMessage websocketMessage, WebsocketConnector connector) {
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
