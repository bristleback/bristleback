package pl.bristleback.server.bristle.engine.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import pl.bristleback.server.bristle.api.DataController;
import pl.bristleback.server.bristle.api.WebsocketConnector;
import pl.bristleback.server.bristle.api.users.UserContext;
import pl.bristleback.server.bristle.security.UsersContainer;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

/**
 * created at 25.11.12
 *
 * @author Pawel Machowski
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultFrontControllerTest {

  @Mock
  private UsersContainer usersContainer;

  @InjectMocks
  private DefaultFrontController frontController = new DefaultFrontController();

  @Mock
  private WebsocketConnector anyConnector;

  @Mock
  private DataController dataController;

  @Before
  public void setUp() throws Exception {
    when(anyConnector.getDataController()).thenReturn(dataController);
  }

  @Test
  public void shouldProcessTextMessage() throws Exception {
    frontController.processCommand(anyConnector, WebsocketOperation.TEXT_FRAME.getOperationCode().getCode(), "sampleParam");
    Mockito.verify(anyConnector.getDataController()).processTextData(eq("sampleParam"), Mockito.any(UserContext.class));
  }

  @Test
  public void shouldProcessBinaryMessage() throws Exception {
    frontController.processCommand(anyConnector, WebsocketOperation.BINARY_FRAME.getOperationCode().getCode(), new byte[]{1});
    Mockito.verify(anyConnector.getDataController()).processBinaryData(eq(new byte[]{1}), Mockito.any(UserContext.class));
  }

  @Test
  public void shouldProcessCloseFrameMessage() throws Exception {
    Object anyObject = new Object();
    frontController.processCommand(anyConnector, WebsocketOperation.CLOSE_FRAME.getOperationCode().getCode(), anyObject);
    Mockito.verify(anyConnector).stop();
  }

  @Test
  public void shouldNotFailWhenProcessingUnknownOperation() throws Exception {
    frontController.processCommand(null, -1, null);
  }
}
