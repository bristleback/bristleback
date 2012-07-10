package pl.bristleback.server.bristle.action.client.strategy;

import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.api.action.ClientActionSender;
import pl.bristleback.server.bristle.authorisation.conditions.SendCondition;
import pl.bristleback.server.bristle.message.BristleMessage;
import pl.bristleback.server.bristle.message.ConditionObjectSender;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-07-08 14:09:36 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component
public class ConditionSenderStrategy implements ClientActionSender<SendCondition> {

  @Override
  public void sendClientAction(SendCondition actionCondition, BristleMessage message, ConditionObjectSender objectSender) throws Exception {
    objectSender.sendNamedMessage(message, message.getName(), actionCondition);
  }
}
