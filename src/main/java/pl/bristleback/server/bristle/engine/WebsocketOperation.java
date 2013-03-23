package pl.bristleback.server.bristle.engine;

import pl.bristleback.server.bristle.api.WebsocketConnector;
import pl.bristleback.server.bristle.api.users.UserContext;
import pl.bristleback.server.bristle.security.UsersContainer;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-09-25 11:59:43 <br/>
 *
 * @author Wojciech Niemiec
 */
public enum WebsocketOperation {

  TEXT_FRAME(OperationCodes.TEXT_FRAME_CODE) {
    public void performOperation(WebsocketConnector connector, UsersContainer usersContainer, Object data) {
      UserContext userContext = usersContainer.getUserContext(connector);
      connector.getDataController().processTextData((String) data, userContext);
    }
  },

  BINARY_FRAME(OperationCodes.BINARY_FRAME_CODE) {
    public void performOperation(WebsocketConnector connector, UsersContainer usersContainer, Object data) {
      UserContext userContext = usersContainer.getUserContext(connector);
      connector.getDataController().processBinaryData((byte[]) data, userContext);
    }
  },

  CLOSE_FRAME(OperationCodes.CLOSE_FRAME_CODE) {
    public void performOperation(WebsocketConnector connector, UsersContainer usersContainer, Object data) {
      connector.stop();
    }
  };

  private OperationCodes operationCode;

  WebsocketOperation(OperationCodes operationCode) {
    this.operationCode = operationCode;
  }

  public OperationCodes getOperationCode() {
    return operationCode;
  }

  public abstract void performOperation(WebsocketConnector connector, UsersContainer usersContainer, Object data);

}
