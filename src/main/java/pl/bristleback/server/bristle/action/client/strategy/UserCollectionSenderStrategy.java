package pl.bristleback.server.bristle.action.client.strategy;

import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.api.action.ClientActionSender;
import pl.bristleback.server.bristle.api.users.IdentifiedUser;
import pl.bristleback.server.bristle.message.BristleMessage;
import pl.bristleback.server.bristle.message.ConditionObjectSender;

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

  @Override
  public void sendClientAction(List<IdentifiedUser> users, BristleMessage message,
                               ConditionObjectSender objectSender) throws Exception {
    objectSender.sendNamedMessage(message, message.getName(), users);
  }
}