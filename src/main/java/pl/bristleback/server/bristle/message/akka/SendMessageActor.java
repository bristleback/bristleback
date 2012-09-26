package pl.bristleback.server.bristle.message.akka;

import akka.actor.UntypedActor;
import org.apache.log4j.Logger;
import pl.bristleback.server.bristle.api.ServerEngine;
import pl.bristleback.server.bristle.api.WebsocketMessage;
import pl.bristleback.server.bristle.serialization.MessageType;

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
