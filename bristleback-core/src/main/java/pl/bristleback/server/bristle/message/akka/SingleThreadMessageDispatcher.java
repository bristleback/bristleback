package pl.bristleback.server.bristle.message.akka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.actor.UntypedActorFactory;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.api.WebsocketConnector;
import pl.bristleback.server.bristle.api.WebsocketMessage;
import pl.bristleback.server.bristle.message.AbstractMessageDispatcher;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;


/**
 * //@todo class description
 * <p/>
 * Created on: 2011-07-05 20:38:01 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component("system.dispatcher.single.threaded")
public class SingleThreadMessageDispatcher extends AbstractMessageDispatcher {
  private static Logger log = Logger.getLogger(SingleThreadMessageDispatcher.class.getName());

  private static final long DELAY = 1000; // in milliseconds

  private boolean dispatcherRunning;
  private final BlockingQueue<WebsocketMessage> messages;
  private ActorRef sendMessageActor;

  public SingleThreadMessageDispatcher() {
    messages = new LinkedBlockingQueue<WebsocketMessage>();
  }

  @Override
  public void addMessage(WebsocketMessage message) {
    messages.add(message);
  }

  @Override
  public void dispatchMessages() throws Exception {
    WebsocketMessage message = messages.poll(DELAY, TimeUnit.MILLISECONDS);
    if (message != null) {
      log.debug("Sending a server message: " + message.getContent());
      if (CollectionUtils.isEmpty(message.getRecipients())) {
        log.debug("Empty or null recipients collection: " + message.getRecipients());
        return;
      }
      sendMessage(message);
    }
  }

  private void sendMessage(WebsocketMessage message) throws Exception {
    for (Object connector : message.getRecipients()) {
      sendMessageActor.tell(new MessageForConnector(message, (WebsocketConnector) connector));
    }
  }

  @Override
  public void startDispatching() {
    if (dispatcherRunning) {
      throw new IllegalStateException("Dispatcher already running.");
    }
    log.info("Starting single threaded message dispatcher");
    ActorSystem system = ActorSystem.create("BristlebackSystem");
    sendMessageActor = system.actorOf(new Props(new UntypedActorFactory() {
      public UntypedActor create() {
        return new SendMessageActor(getServer());
      }
    }), "MessageDispatcherActor");
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    setDispatcherRunning(true);
    executorService.execute(new Dispatcher());
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

  private class Dispatcher implements Runnable {

    public void run() {
      try {
        while (dispatcherRunning) {
          dispatchMessages();
        }
      } catch (Exception e) {
        //nothing
        log.error(e);
      }
    }
  }

}
