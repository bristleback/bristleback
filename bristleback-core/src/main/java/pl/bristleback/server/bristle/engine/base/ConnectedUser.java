package pl.bristleback.server.bristle.engine.base;

import pl.bristleback.server.bristle.api.WebsocketConnector;
import pl.bristleback.server.bristle.api.users.UserContext;

/**
 * Class used to manage connection between {@link UserContext} and {@link WebsocketConnector}.
 * Represents single user connected to BristleBack server.
 * <p/>
 * Pawel Machowski
 * created at 03.05.12 16:36
 */
public class ConnectedUser {

  private UserContext userContext;

  private WebsocketConnector connector;

  public ConnectedUser(UserContext userContext, WebsocketConnector connector) {
    this.userContext = userContext;
    this.connector = connector;
  }

  public UserContext getUserContext() {
    return userContext;
  }

  public WebsocketConnector getConnector() {
    return connector;
  }
}
