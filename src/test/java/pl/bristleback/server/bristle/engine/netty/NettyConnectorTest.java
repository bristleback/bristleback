package pl.bristleback.server.bristle.engine.netty;


import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import pl.bristleback.server.bristle.api.WebsocketMessage;
import pl.bristleback.server.bristle.message.BaseMessage;
import pl.bristleback.server.bristle.serialization.MessageType;
import pl.bristleback.server.mock.MockFactory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Pawel Machowski
 * created at 11.02.12 15:10
 */
public class NettyConnectorTest {

  private NettyConnector connector;

  @Before
  public void setUp() throws Exception {
    connector = new NettyConnector(null, MockFactory.getMockServerEngine(), null);
  }

  @Test
  public void shouldAuthoriseAllNotActionMessages() throws Exception {
    //GIVEN not action message class
    WebsocketMessage message = new BaseMessage(MessageType.TEXT);

    //WHEN connector checks rights
    boolean isAuthorised = connector.isAuthorisedToHandleMessage(message);

    //THEN action should be authorised
    assertThat(isAuthorised, is(true));
  }

  @Test
  public void shouldAuthoriseActionMessage() throws Exception {
    //GIVEN always authorise policy
    connector.setAuthorisationPolicy(new AlwaysAuthorisePolicy());
    //AND action message
    WebsocketMessage actionMessage = new BaseMessage(MessageType.TEXT);

    //WHEN connector checks rights
    boolean isAuthorised = connector.isAuthorisedToHandleMessage(actionMessage);

    //THEN
    assertThat(isAuthorised, is(true));
  }

  @Test
  @Ignore
  public void shouldNotAuthoriseActionMessage() throws Exception {
    //GIVEN never authorise policy
    connector.setAuthorisationPolicy(new NeverAuthorisePolicy());
    //AND action message
    WebsocketMessage actionMessage = new BaseMessage(MessageType.TEXT);

    //WHEN connector checks rights
    boolean isAuthorised = connector.isAuthorisedToHandleMessage(actionMessage);

    //THEN
    assertThat(isAuthorised, is(false));
  }

  /**
   * Stub class for testing success authorisation
   */
  private class AlwaysAuthorisePolicy implements AuthorisationPolicy {
    @Override
    public boolean isActionAuthorised(String action) {
      return true;
    }
  }

  /**
   * Stub class for testing failure authorisation
   */
  private class NeverAuthorisePolicy implements AuthorisationPolicy {
    @Override
    public boolean isActionAuthorised(String action) {
      return false
        ;
    }
  }
}
