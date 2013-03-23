package pl.bristleback.server.bristle.security.authentication;

import org.apache.log4j.Logger;
import pl.bristleback.server.bristle.api.annotations.ObjectSender;
import pl.bristleback.server.bristle.api.users.UserContext;
import pl.bristleback.server.bristle.message.BristleMessage;
import pl.bristleback.server.bristle.message.ConditionObjectSender;

import java.util.Collections;

/**
 * This component is used to send messages about events related with authentication process.
 * <p/>
 * Created on: 23.03.13 12:20 <br/>
 *
 * @author Wojciech Niemiec
 */
public class AuthenticationInformer {

  private static final String LOGOUT_MESSAGE_NAME = "SystemAuth.logout";

  private static Logger log = Logger.getLogger(AuthenticationInformer.class.getName());

  @ObjectSender
  private ConditionObjectSender objectSender;

  public void sendLogoutInformation(UserContext userContext) {
    BristleMessage message = new BristleMessage()
      .withName(LOGOUT_MESSAGE_NAME);

    try {
      objectSender.sendMessage(message, Collections.singletonList(userContext));
    } catch (Exception e) {
      log.error("Bristleback could not send system logout message", e);
    }
  }
}
