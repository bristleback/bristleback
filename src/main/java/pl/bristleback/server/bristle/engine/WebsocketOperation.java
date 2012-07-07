package pl.bristleback.server.bristle.engine;

import pl.bristleback.server.bristle.api.WebsocketConnector;
import pl.bristleback.server.bristle.api.users.IdentifiedUser;
import pl.bristleback.server.bristle.authorisation.user.UsersContainer;

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
      IdentifiedUser user = usersContainer.getUserByConnector(connector);
      connector.getDataController().processTextData((String) data, user);
    }
  },

  BINARY_FRAME(OperationCodes.BINARY_FRAME_CODE) {
    public void performOperation(WebsocketConnector connector, UsersContainer usersContainer, Object data) {
      IdentifiedUser user = usersContainer.getUserByConnector(connector);
      connector.getDataController().processBinaryData((byte[]) data, user);
    }
  },

  CLOSE_FRAME(OperationCodes.CLOSE_FRAME_CODE) {
    public void performOperation(WebsocketConnector connector, UsersContainer usersContainer, Object data) {
      connector.stop(); //possibly duplicated call to stop method in case of time
      usersContainer.removeUser(connector);
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
