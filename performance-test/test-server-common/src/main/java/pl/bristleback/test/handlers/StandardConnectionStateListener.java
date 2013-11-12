package pl.bristleback.test.handlers;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.api.ConnectionStateListener;
import pl.bristleback.server.bristle.api.users.UserContext;
import pl.bristleback.server.bristle.listener.ConnectionStateListenerChain;

@Component
public class StandardConnectionStateListener implements ConnectionStateListener<UserContext> {

  private static Logger log = Logger.getLogger(StandardConnectionStateListener.class.getName());

  @Override
  public void userConnected(UserContext userContext, ConnectionStateListenerChain connectionStateListenerChain) {
    log.info("listener says that new user was added - id: " + userContext.getId());
  }

  @Override
  public void userDisconnected(UserContext userContext, ConnectionStateListenerChain connectionStateListenerChain) {
    log.info("listener says that the user has disconnected - id: " + userContext.getId());
  }
}
