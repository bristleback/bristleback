package pl.bristleback.server.bristle.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.engine.base.ConnectedUser;
import pl.bristleback.server.bristle.exceptions.UserNotAuthenticatedException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * //@todo class description
 * <p/>
 * Created on: 18.02.13 19:12 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component
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
    String connectorId = user.getConnector().getConnectorId();
    UserAuthentication authentication = connectorIdToAuthenticationMappings.get(connectorId);
    if(authentication == null) {
      throw new UserNotAuthenticatedException();
    }
    authentication.invalidate();
    concurrentAuthentications.remove(authentication.getAuthenticatedUser().getUsername());
  }
}
