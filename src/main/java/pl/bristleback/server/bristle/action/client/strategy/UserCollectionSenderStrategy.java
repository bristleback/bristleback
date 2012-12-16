package pl.bristleback.server.bristle.action.client.strategy;

import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.action.client.ClientActionInformation;
import pl.bristleback.server.bristle.api.action.ClientActionSender;
import pl.bristleback.server.bristle.api.users.IdentifiedUser;
import pl.bristleback.server.bristle.authorisation.user.UsersContainer;
import pl.bristleback.server.bristle.message.BristleMessage;
import pl.bristleback.server.bristle.message.ConditionObjectSender;

import javax.inject.Inject;
import java.util.List;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-07-23 16:43:28 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component
public class UserCollectionSenderStrategy implements ClientActionSender<List<IdentifiedUser>> {

  @Inject
  private UsersContainer connectedUsers;

  @Override
  public void sendClientAction(List<IdentifiedUser> users, BristleMessage message,
                               ConditionObjectSender objectSender, ClientActionInformation actionInformation) throws Exception {

    objectSender.sendMessage(message, actionInformation.getSerialization(), connectedUsers.getConnectorsByUsers(users));
  }
}