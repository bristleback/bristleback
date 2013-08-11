package pl.bristleback.client;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import pl.bristleback.client.actions.arguments.Arguments;
import pl.bristleback.client.actions.arguments.BasicArgumentsHandler;
import pl.bristleback.client.api.onmessage.MessageHandler;
import pl.bristleback.client.mockinstance.BristlebacTestInstance;
import pl.bristleback.client.serialization.Person;
import pl.bristleback.common.serialization.message.BristleMessage;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

/**
 * <p/>
 * Created on: 31.07.13 19:59 <br/>
 *
 * @author Pawel Machowski
 */
//@RunWith(MockitoJUnitRunner.class)
public class JettyClientTest {

  private static BristlebacTestInstance bristlebacTestInstance;

  @BeforeClass
  public static void setUp() throws Exception {
    bristlebacTestInstance = new BristlebacTestInstance();
    bristlebacTestInstance.startServer();
  }

  @AfterClass
  public static void tearDown() throws Exception {
    bristlebacTestInstance.startServer();
  }

  @Test
  public void singleServerActionWithResponse() throws Exception {
    JettyClient jettyClient = new JettyClient("TODO");
    jettyClient.connect();

    //given
    TestMethodHandler testMethodHandler = new TestMethodHandler();
    jettyClient.actionController().registerActionClass("TestAction")
      .withMethod("testMethod", testMethodHandler);

    //when
    sendNewPersonMessage(jettyClient, "TestAction.testMethod");
    waitForServerResponse();

    //then
    assertEquals(new Person().getName(), testMethodHandler.getResult().getName());
  }

  @Test
  public void actionWithTwoMethods() throws Exception {
    JettyClient jettyClient = new JettyClient("TODO");
    jettyClient.connect();

    //given
    TestMethodHandler testMethodHandler = new TestMethodHandler();
    jettyClient.actionController().registerActionClass("TestAction")
      .withMethod("testMethod", testMethodHandler)
      .withMethod("incrementAge", testMethodHandler);

    //when
    sendNewPersonMessage(jettyClient, "TestAction.testMethod");
    sendNewPersonMessage(jettyClient, "TestAction.incrementAge");
    waitForServerResponse();

    //then
    assertEquals(new Person().getAge() + 1, testMethodHandler.getResult().getAge());
  }

  private void waitForServerResponse() throws InterruptedException {
    TimeUnit.SECONDS.sleep(5); //TODO
  }


  class TestMethodHandler extends BasicArgumentsHandler {
    private Person result;

    @Override
    public void processArguments(Arguments arguments) {
      this.result = arguments.get(0, Person.class);
    }

    public Person getResult() {
      return result;
    }
  }

  class ObjectPayloadHandler implements MessageHandler<Object> {
    @Override
    public void onMessage(Object payload) {
      System.out.println(payload);
    }
  }

  private void sendNewPersonMessage(JettyClient jettyClient, String actionName) {
    BristleMessage<Person[]> bristleMessage = new BristleMessage<Person[]>();
    bristleMessage.setName(actionName);
    bristleMessage.setPayload(new Person[]{new Person()});
    jettyClient.sendMessage(bristleMessage);
  }
}
