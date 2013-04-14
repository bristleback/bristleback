package pl.bristleback.server.bristle.api.action;

import pl.bristleback.server.bristle.action.client.ClientActionInformation;
import pl.bristleback.server.bristle.message.BristleMessage;
import pl.bristleback.server.bristle.message.ConditionObjectSender;

/**
 * By default, client actions must return one of the fallowing object types:
 * <ol>
 * <li>{@link SendCondition} implementation</li>
 * <li>{@link pl.bristleback.server.bristle.api.users.UserContext} implementation</li>
 * <li>List of {@link pl.bristleback.server.bristle.api.users.UserContext} implementations</li>
 * </ol>
 * To extend this behaviour, you need to create a class implementing this interface.
 * <p/>
 * Created on: 2012-07-07 21:08:16 <br/>
 *
 * @author Wojciech Niemiec
 */
public interface ClientActionSender<T> {

  /**
   * Sends message to one or more clients using action condition given as parameter.
   * Type of action condition is the type that client action should return to be processed by this client action sender implementation.
   *
   * @param actionCondition   action condition used to determine recipients of client action message.
   * @param message           message to sent.
   * @param objectSender      object sender.
   * @param actionInformation information about client action that sent actual action condition.
   * @throws Exception
   */
  void sendClientAction(T actionCondition, BristleMessage message, ConditionObjectSender objectSender, ClientActionInformation actionInformation) throws Exception;
}
