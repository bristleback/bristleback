package pl.bristleback.server.bristle.engine.controller;

import pl.bristleback.server.bristle.api.WebsocketConnector;
import pl.bristleback.server.bristle.api.users.UserContext;
import pl.bristleback.server.bristle.engine.OperationCode;
import pl.bristleback.server.bristle.security.UsersContainer;

/**
 * This is a helper class used by {@link DefaultFrontController} to handle incoming Websocket messages.
 * <p/>
 * Created on: 2011-09-25 11:59:43 <br/>
 *
 * @author Wojciech Niemiec
 */
public enum WebsocketOperation {

  TEXT_FRAME(OperationCode.TEXT_FRAME_CODE) {
    public void performOperation(WebsocketConnector connector, UsersContainer usersContainer, Object data) {
      UserContext userContext = usersContainer.getUserContext(connector);
      connector.getDataController().processTextData((String) data, userContext);
    }
  },

  BINARY_FRAME(OperationCode.BINARY_FRAME_CODE) {
    public void performOperation(WebsocketConnector connector, UsersContainer usersContainer, Object data) {
      UserContext userContext = usersContainer.getUserContext(connector);
      connector.getDataController().processBinaryData((byte[]) data, userContext);
    }
  },

  CLOSE_FRAME(OperationCode.CLOSE_FRAME_CODE) {
    public void performOperation(WebsocketConnector connector, UsersContainer usersContainer, Object data) {
      connector.stop();
    }
  };

  private OperationCode operationCode;

  WebsocketOperation(OperationCode operationCode) {
    this.operationCode = operationCode;
  }

  public OperationCode getOperationCode() {
    return operationCode;
  }

  public abstract void performOperation(WebsocketConnector connector, UsersContainer usersContainer, Object data);

}
