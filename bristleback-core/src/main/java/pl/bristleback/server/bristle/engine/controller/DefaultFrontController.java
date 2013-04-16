package pl.bristleback.server.bristle.engine.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.api.FrontController;
import pl.bristleback.server.bristle.api.WebsocketConnector;
import pl.bristleback.server.bristle.security.UsersContainer;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * This implementation of front controller can handle three kinds of messages:
 * <ul>
 * <li>Text message</li>
 * <li>Binary message</li>
 * <li>Close connection message</li>
 * </ul>
 * Future version should bring possibility to handle ping/pong messages.
 * <p/>
 * Created on: 2011-11-21 18:35:01 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component("defaultFrontController")
public class DefaultFrontController implements FrontController {

  private static Logger log = Logger.getLogger(DefaultFrontController.class.getName());

  private Map<Integer, WebsocketOperation> operations;

  @Inject
  private UsersContainer usersContainer;

  public DefaultFrontController() {
    operations = new HashMap<Integer, WebsocketOperation>();
    for (WebsocketOperation operation : WebsocketOperation.values()) {
      operations.put(operation.getOperationCode().getCode(), operation);
    }
  }

  public void processCommand(WebsocketConnector connector, int operationCode, Object data) {
    WebsocketOperation operation = operations.get(operationCode);
    if (operation != null) {
      operation.performOperation(connector, usersContainer, data);
    } else {
      log.warn("Cannot perform operation with code " + operationCode + ", operation cannot be found.");
    }
  }
}
