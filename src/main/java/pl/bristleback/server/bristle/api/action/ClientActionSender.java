package pl.bristleback.server.bristle.api.action;

import pl.bristleback.server.bristle.message.BristleMessage;
import pl.bristleback.server.bristle.message.ConditionObjectSender;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-07-07 21:08:16 <br/>
 *
 * @author Wojciech Niemiec
 */
public interface ClientActionSender<T> {

  void sendClientAction(T actionCondition, BristleMessage message, ConditionObjectSender objectSender) throws Exception;
}
