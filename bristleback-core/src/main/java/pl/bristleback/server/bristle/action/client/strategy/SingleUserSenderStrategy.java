package pl.bristleback.server.bristle.action.client.strategy;

import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.action.client.ClientActionInformation;
import pl.bristleback.server.bristle.api.action.ClientActionSender;
import pl.bristleback.server.bristle.api.users.UserContext;
import pl.bristleback.server.bristle.message.BristleMessage;
import pl.bristleback.server.bristle.message.ConditionObjectSender;
import pl.bristleback.server.bristle.security.UsersContainer;

import javax.inject.Inject;
import java.util.Collections;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-07-08 14:08:09 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component
public class SingleUserSenderStrategy implements ClientActionSender<UserContext> {

  @Inject
  private UsersContainer connectedUsers;

  @Override
  public void sendClientAction(UserContext user, BristleMessage message, ConditionObjectSender objectSender, ClientActionInformation actionInformation) throws Exception {
    objectSender.sendMessage(message, actionInformation.getSerialization(), connectedUsers.getConnectorsByUsers(Collections.singletonList(user)));
  }
}
