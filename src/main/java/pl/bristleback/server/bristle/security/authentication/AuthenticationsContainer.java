package pl.bristleback.server.bristle.security.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import pl.bristleback.server.bristle.api.WebsocketConnector;
import pl.bristleback.server.bristle.engine.base.ConnectedUser;
import pl.bristleback.server.bristle.exceptions.UserNotAuthenticatedException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class contains all information about currently open authentications.
 * <p/>
 * Created on: 18.02.13 19:12 <br/>
 *
 * @author Wojciech Niemiec
 */
public class AuthenticationsContainer {

  private Map<String, UserAuthentication> connectorIdToAuthenticationMappings;

  private Map<String, List<UserAuthentication>> concurrentAuthentications;

  @Autowired
  private AuthenticationConfiguration authenticationConfiguration;

  public AuthenticationsContainer() {
    concurrentAuthentications = Collections.synchronizedMap(new HashMap<String, List<UserAuthentication>>());
    connectorIdToAuthenticationMappings = new HashMap<String, UserAuthentication>();
  }

  public void addAndInvalidatePreviousIfNecessary(UserAuthentication userAuthentication) {
    String username = userAuthentication.getAuthenticatedUser().getUsername();
    if (isLimitReached(username)) {
      UserAuthentication authenticationToBeInvalidated = concurrentAuthentications.get(username).get(0);
      authenticationToBeInvalidated.invalidate();
    }
    concurrentAuthentications.get(username).add(userAuthentication);

    connectorIdToAuthenticationMappings.put(userAuthentication.getUser().getConnector().getConnectorId(), userAuthentication);
  }

  private boolean isLimitReached(String username) {
    int concurrentUsersLimit = authenticationConfiguration.getMaximumAuthenticationsPerUsername();
    if (!concurrentAuthentications.containsKey(username)) {
      concurrentAuthentications.put(username, new ArrayList<UserAuthentication>());
    }
    return concurrentAuthentications.get(username).size() >= concurrentUsersLimit;
  }

  public void logout(ConnectedUser user) {
    UserAuthentication authentication = getAuthentication(user);
    authentication.invalidate();
    concurrentAuthentications.remove(authentication.getAuthenticatedUser().getUsername());
  }

  /**
   * Gets authentication representation for given connection.
   *
   * @param user connection representation.
   * @return user authentication object.
   * @throws UserNotAuthenticatedException if there is not authentication for given connection
   *                                       or authentication is invalidated.
   */
  public UserAuthentication getAuthentication(ConnectedUser user) {
    String connectorId = user.getConnector().getConnectorId();
    UserAuthentication authentication = connectorIdToAuthenticationMappings.get(connectorId);
    if (authentication == null || !authentication.isValid()) {
      throw new UserNotAuthenticatedException();
    }
    return authentication;
  }

  public boolean hasValidAuthenticationForConnection(WebsocketConnector connector) {
    UserAuthentication authentication = connectorIdToAuthenticationMappings.get(connector.getConnectorId());
    return authentication != null && authentication.isValid();
  }
}
