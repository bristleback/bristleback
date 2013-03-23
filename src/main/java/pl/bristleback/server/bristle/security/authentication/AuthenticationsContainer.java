package pl.bristleback.server.bristle.security.authentication;

import org.springframework.beans.factory.annotation.Autowired;
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

    connectorIdToAuthenticationMappings.put(userAuthentication.getUserContext().getId(), userAuthentication);
  }

  private boolean isLimitReached(String username) {
    int concurrentUsersLimit = authenticationConfiguration.getMaximumAuthenticationsPerUsername();
    if (!concurrentAuthentications.containsKey(username)) {
      concurrentAuthentications.put(username, new ArrayList<UserAuthentication>());
    }
    return concurrentAuthentications.get(username).size() >= concurrentUsersLimit;
  }

  public void logout(String connectionId) {
    UserAuthentication authentication = getAuthentication(connectionId);
    authentication.invalidate();
    concurrentAuthentications.remove(authentication.getAuthenticatedUser().getUsername());
  }

  /**
   * Gets authentication representation for given connection.
   *
   * @param connectionId connection identification.
   * @return user authentication object.
   * @throws UserNotAuthenticatedException if there is not authentication for given connection
   *                                       or authentication is invalidated.
   */
  public UserAuthentication getAuthentication(String connectionId) {
    UserAuthentication authentication = connectorIdToAuthenticationMappings.get(connectionId);
    if (authentication == null || !authentication.isValid()) {
      throw new UserNotAuthenticatedException();
    }
    return authentication;
  }

  public boolean hasValidAuthenticationForConnection(String connectionId) {
    UserAuthentication authentication = connectorIdToAuthenticationMappings.get(connectionId);
    return authentication != null && authentication.isValid();
  }
}
