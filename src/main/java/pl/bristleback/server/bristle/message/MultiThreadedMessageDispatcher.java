package pl.bristleback.server.bristle.message;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.actor.UntypedActorFactory;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.akka.MessageForConnector;
import pl.bristleback.server.bristle.akka.SendMessageActor;
import pl.bristleback.server.bristle.api.WebsocketConnector;
import pl.bristleback.server.bristle.api.WebsocketMessage;

/**
 * created at 23.09.12
 *
 * @author Pawel Machowski
 */
@Component("system.dispatcher.multi.threaded")
public class MultiThreadedMessageDispatcher extends AbstractMessageDispatcher {
  private static Logger log = Logger.getLogger(MultiThreadedMessageDispatcher.class.getName());

  private boolean dispatcherRunning;
  private ActorRef sendMessageActor;

  @Override
  public void addMessage(WebsocketMessage message) {
    if (dispatcherRunning) {
      for (Object connector : message.getRecipients()) {
        sendMessageActor.tell(new MessageForConnector(message, (WebsocketConnector) connector));
      }
    }
  }

  @Override
  public void dispatchMessages() throws Exception {
    //empty, message is dispatched as soon at it's sent do dispacher, Akka library handles it then.
  }

  @Override
  public void startDispatching() {
    if (dispatcherRunning) {
      throw new IllegalStateException("Dispatcher already running.");
    }
    log.info("Starting multi threaded message dispatcher");
    ActorSystem system = ActorSystem.create("BristlebackSystem");
    sendMessageActor = system.actorOf(new Props(new UntypedActorFactory() {
      public UntypedActor create() {
        return new SendMessageActor(getServer());
      }
    }), "MessageDispatcherActor");
    setDispatcherRunning(true);
  }

  @Override
  public void stopDispatching() {
    if (!dispatcherRunning) {
      throw new IllegalStateException("Dispatcher is not running yet");
    }
    setDispatcherRunning(false);
  }


  private void setDispatcherRunning(boolean dispatcherRunning) {
    this.dispatcherRunning = dispatcherRunning;
  }
}
