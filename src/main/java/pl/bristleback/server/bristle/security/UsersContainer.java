package pl.bristleback.server.bristle.security;

import javolution.util.FastMap;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.api.WebsocketConnector;
import pl.bristleback.server.bristle.api.action.SendCondition;
import pl.bristleback.server.bristle.api.users.UserContext;
import pl.bristleback.server.bristle.api.users.UserContextFactory;
import pl.bristleback.server.bristle.engine.base.ConnectedUser;
import pl.bristleback.server.bristle.security.authorisation.conditions.AllUsersCondition;

import javax.inject.Inject;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Class holding references to all currently connected users. Manages connection between frameworks based abstraction of
 * user context ({@link pl.bristleback.server.bristle.api.users.UserContext}) and WebSocket internal implementation ({@link WebsocketConnector}).
 * Provides convenient methods for transforming user context to WebSocket connector and vice-versa.
 *
 * @author Pawel Machowski created at 01.05.12 14:04
 */
@Component
public class UsersContainer {

  @Inject
  private UserContextFactory userContextFactory;

  private Map<String, ConnectedUser> connectedUsers = new FastMap<String, ConnectedUser>().setShared(true);

  public UserContext newUser(WebsocketConnector connector) {
    UserContext newUser = userContextFactory.createNewUserContext();
    newUser.setId(connector.getConnectorId());
    connectedUsers.put(
      connector.getConnectorId(),
      new ConnectedUser(newUser, connector)
    );
    return newUser;
  }

  public UserContext getUserContext(WebsocketConnector connector) {
    return connectedUsers.get(connector.getConnectorId()).getUserContext();
  }

  @SuppressWarnings("unchecked")
  public List<WebsocketConnector> getConnectorsByCondition(final SendCondition condition) {
    List<WebsocketConnector> applicableConnectors = new LinkedList<WebsocketConnector>();
    for (Map.Entry<String, ConnectedUser> user : connectedUsers.entrySet()) {
      if (condition.isApplicable(user.getValue().getUserContext())) {
        applicableConnectors.add(user.getValue().getConnector());
      }
    }
    return applicableConnectors;
  }

  public List<WebsocketConnector> getConnectorsByCondition(List<UserContext> usersSubset, final SendCondition condition) {
    List<UserContext> userList = getUsersMeetingCondition(usersSubset, condition);
    List<WebsocketConnector> applicableConnectors = new LinkedList<WebsocketConnector>();

    for (Map.Entry<String, ConnectedUser> user : connectedUsers.entrySet()) {
      for (UserContext identifiedUser : userList) {
        if (identifiedUser.getId().equals(user.getValue().getUserContext().getId())) {
          applicableConnectors.add(user.getValue().getConnector());
        }
      }
    }
    return applicableConnectors;
  }

  public List<WebsocketConnector> getConnectorsByUsers(List<UserContext> usersSubset) {
    return getConnectorsByCondition(usersSubset, AllUsersCondition.getInstance());
  }

  public WebsocketConnector getConnectorByUser(UserContext user) {
    return getConnectorsByCondition(Collections.singletonList(user), AllUsersCondition.getInstance()).get(0);
  }

  @SuppressWarnings("unchecked")
  private List<UserContext> getUsersMeetingCondition(List<UserContext> usersSubset, final SendCondition condition) {
    return (List<UserContext>) CollectionUtils.select(usersSubset, new Predicate() {
      @Override
      public boolean evaluate(Object object) {
        return condition.isApplicable((UserContext) object);
      }
    });
  }

  public void removeUser(WebsocketConnector connector) {
    connectedUsers.remove(connector.getConnectorId());
  }
}
