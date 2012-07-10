package pl.bristleback.server.bristle.action.client.strategy;

import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.api.action.ClientActionSender;
import pl.bristleback.server.bristle.api.users.IdentifiedUser;
import pl.bristleback.server.bristle.message.BristleMessage;
import pl.bristleback.server.bristle.message.ConditionObjectSender;

import java.util.Collections;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-07-08 14:08:09 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component
public class SingleUserSenderStrategy implements ClientActionSender<IdentifiedUser> {

  @Override
  public void sendClientAction(IdentifiedUser user, BristleMessage message, ConditionObjectSender objectSender) throws Exception {
    objectSender.sendNamedMessage(message, message.getName(), Collections.singletonList(user));
  }
}
