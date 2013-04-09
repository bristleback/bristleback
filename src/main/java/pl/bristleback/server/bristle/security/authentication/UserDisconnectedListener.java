package pl.bristleback.server.bristle.security.authentication;

import org.apache.log4j.Logger;
import pl.bristleback.server.bristle.api.ConnectionStateListener;
import pl.bristleback.server.bristle.api.users.UserContext;
import pl.bristleback.server.bristle.listener.ConnectionStateListenerChain;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * //@todo class description
 * <p/>
 * Created on: 14.03.13 13:18 <br/>
 *
 * @author Wojciech Niemiec
 */
public class UserDisconnectedListener implements ConnectionStateListener<UserContext> {

  private static Logger log = Logger.getLogger(UserDisconnectedListener.class.getName());

  @Inject
  @Named("bristleAuthenticationsContainer")
  private AuthenticationsContainer authenticationsContainer;

  @Override
  public void userConnected(UserContext userContext, ConnectionStateListenerChain connectionStateListenerChain) {

  }

  @Override
  public void userDisconnected(UserContext userContext, ConnectionStateListenerChain connectionStateListenerChain) {
    if (authenticationsContainer.hasValidAuthenticationForConnection(userContext.getId())) {
      String username = authenticationsContainer.getAuthentication(userContext.getId()).getAuthenticatedUser().getUsername();
      authenticationsContainer.logout(userContext.getId());
      log.debug("User \"" + username + "\" has been logged out.");
    }
  }
}
