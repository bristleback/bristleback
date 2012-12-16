package pl.bristleback.server.bristle.action.client.strategy;

import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.action.client.ClientActionInformation;
import pl.bristleback.server.bristle.api.WebsocketConnector;
import pl.bristleback.server.bristle.api.action.ClientActionSender;
import pl.bristleback.server.bristle.api.action.SendCondition;
import pl.bristleback.server.bristle.authorisation.user.UsersContainer;
import pl.bristleback.server.bristle.message.BristleMessage;
import pl.bristleback.server.bristle.message.ConditionObjectSender;

import javax.inject.Inject;
import java.util.List;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-07-08 14:09:36 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component
public class ConditionSenderStrategy implements ClientActionSender<SendCondition> {

  @Inject
  private UsersContainer connectedUsers;

  @Override
  public void sendClientAction(SendCondition actionCondition, BristleMessage message, ConditionObjectSender objectSender, ClientActionInformation actionInformation) throws Exception {
    List<WebsocketConnector> connectors = connectedUsers.getConnectorsByCondition(actionCondition);
    objectSender.sendMessage(message, actionInformation.getSerialization(), connectors);
  }
}
