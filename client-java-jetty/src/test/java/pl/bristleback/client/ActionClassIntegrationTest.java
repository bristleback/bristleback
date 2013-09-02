package pl.bristleback.client;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
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
public class ActionClassIntegrationTest {

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

  @Test
  public void twoActions() throws Exception {
    JettyClient jettyClient = new JettyClient("TODO");
    jettyClient.connect();

    //given
    TestMethodHandler testMethodHandler = new TestMethodHandler();
    jettyClient.actionController().registerActionClass("TestAction")
      .withMethod("testMethod", testMethodHandler);
    jettyClient.actionController().registerActionClass("PersonActionClass")
      .withMethod("resetAge", testMethodHandler);


    sendNewPersonMessage(jettyClient, "TestAction.testMethod");
    waitForServerResponse();
    assertEquals(new Person().getAge(), testMethodHandler.getResult().getAge());

    sendNewPersonMessage(jettyClient, "PersonActionClass.resetAge");
    waitForServerResponse();
    assertEquals(0, testMethodHandler.getResult().getAge());

  }

  @Test
  public void clientAction() throws Exception {
    JettyClient jettyClient = new JettyClient("TODO");
    jettyClient.connect();

    //given
    TestMethodHandler testMethodHandler = new TestMethodHandler();
    jettyClient.actionController().registerActionClass("TestAction")
      .withMethod("testMethod", testMethodHandler);
    jettyClient.actionController().registerActionClass("PersonActionClass")
      .withMethod("resetAge", testMethodHandler);


    sendNewPersonMessage(jettyClient, "TestAction.testMethod");
    waitForServerResponse();
    assertEquals(new Person().getAge(), testMethodHandler.getResult().getAge());

    sendNewPersonMessage(jettyClient, "PersonActionClass.resetAge");
    waitForServerResponse();
    assertEquals(0, testMethodHandler.getResult().getAge());

  }



  private void waitForServerResponse() throws InterruptedException {
    TimeUnit.SECONDS.sleep(5); //TODO
  }


  private void sendNewPersonMessage(JettyClient jettyClient, String actionName) {
    BristleMessage<Person[]> bristleMessage = new BristleMessage<Person[]>();
    bristleMessage.setName(actionName);
    bristleMessage.setPayload(new Person[]{new Person()});
    jettyClient.sendMessage(bristleMessage);
  }
}
