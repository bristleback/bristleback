package pl.bristleback.client;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import pl.bristleback.client.mockinstance.BristlebacTestInstance;
import pl.bristleback.client.serialization.Person;
import pl.bristleback.common.serialization.message.BristleMessage;

import java.util.concurrent.TimeUnit;

/**
 * <p/>
 * Created on: 01.09.13 21:16 <br/>
 *
 * @author Pawel Machowski
 */
@Ignore("not implemented yet")
public class ClientActionClassIntegrationTest {

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


/*
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
*/


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
