package pl.bristleback.client;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import pl.bristleback.client.mockinstance.BristlebacTestInstance;
import pl.bristleback.client.serialization.Person;
import pl.bristleback.common.serialization.message.BristleMessage;

import java.util.concurrent.TimeUnit;

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
  @Ignore
  public void testClient() throws Exception {
    JettyClient jettyClient = new JettyClient("TODO");
    jettyClient.connect();
    BristleMessage bristleMessage = new BristleMessage();
    bristleMessage.setName("TestAction.testMethod");
    bristleMessage.setPayload(new Person());
    jettyClient.sendMessage(bristleMessage);

    TimeUnit.SECONDS.sleep(5); //TODO

  }
}
