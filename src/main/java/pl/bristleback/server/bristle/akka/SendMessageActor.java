package pl.bristleback.server.bristle.akka;

import akka.actor.UntypedActor;
import org.apache.log4j.Logger;
import pl.bristleback.server.bristle.api.ServerEngine;
import pl.bristleback.server.bristle.api.WebsocketMessage;
import pl.bristleback.server.bristle.serialization.MessageType;

/**
 * created at 16.09.12
 *
 * @author Pawel Machowski
 */
public class SendMessageActor extends UntypedActor {
  private static Logger log = Logger.getLogger(SendMessageActor.class.getName());

  private ServerEngine server;

  public SendMessageActor(ServerEngine seserverEnginever) {
    this.server = seserverEnginever;
  }

  // message handler
  public void onReceive(Object message) throws Exception {
    ActorMessage actorMessage = (ActorMessage) message;
    WebsocketMessage websocketMessage = actorMessage.getWebsocketMessage();
    if (websocketMessage.getMessageType() == MessageType.TEXT) {
      server.sendMessage(actorMessage.getConnector(), (String) websocketMessage.getContent());
    } else if (websocketMessage.getMessageType() == MessageType.BINARY) {
      server.sendMessage(actorMessage.getConnector(), (byte[]) websocketMessage.getContent());
    } else {
      log.debug("Cannot send a websocketMessage, unknown type of websocketMessage " + websocketMessage.getMessageType());
    }
  }

}
