package pl.bristleback.server.bristle.message.akka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.typesafe.config.ConfigFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import pl.bristleback.server.bristle.api.ServerEngine;
import pl.bristleback.server.bristle.api.WebsocketConnector;
import pl.bristleback.server.bristle.message.BaseMessage;
import pl.bristleback.server.bristle.message.MessageType;

import java.util.UUID;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;

/**
 * <p/>
 * Created on: 01.05.13 12:29 <br/>
 *
 * @author Pawel Machowski
 */
public class SendMessageActorTest {

  private static ActorSystem akkaSystem = ActorSystem.create("akka-test-system",  ConfigFactory.load().getConfig("akka-test-system"));

  @Mock
  private ServerEngine serverEngine;
  @Mock
  private WebsocketConnector websocketConnector;

  private ActorRef sendMessageActor;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    String randomActorName = UUID.randomUUID().toString();
    sendMessageActor = akkaSystem.actorOf(ActorFactory.defaultSendMessageActor(serverEngine), randomActorName);
  }

  @Test
  public void shouldProcessTextMessage() throws Exception {
    //given
    BaseMessage textMessage = new BaseMessage(MessageType.TEXT);

    //when
    sendMessageActor.tell(new MessageForConnector(textMessage, websocketConnector));
    waitForAkkaThread();

    //then
    Mockito.verify(serverEngine, times(1)).sendMessage(eq(websocketConnector), Mockito.anyString());
  }

  @Test
  public void shouldProcessBinaryMessage() throws Exception {
    //given
    BaseMessage textMessage = new BaseMessage(MessageType.BINARY);

    //when
    sendMessageActor.tell(new MessageForConnector(textMessage, websocketConnector));
    waitForAkkaThread();

    //then
    Mockito.verify(serverEngine, times(1)).sendMessage(eq(websocketConnector), any(byte[].class));
  }

  @Test
  public void shouldNotProcessUnknownMessage() throws Exception {
    //given
    BaseMessage textMessage = new BaseMessage(null);

    //when
    sendMessageActor.tell(new MessageForConnector(textMessage, websocketConnector));
    waitForAkkaThread();

    //then
    Mockito.verifyZeroInteractions(serverEngine);
  }

  private void waitForAkkaThread() throws InterruptedException {
    Thread.sleep(10);
  }
}
