package pl.bristleback.server.bristle.action;

import pl.bristleback.server.bristle.message.BristleMessage;
import pl.bristleback.server.bristle.utils.StringUtils;

/**
 * This factory class contains methods used to create various types of server action messages.
 * <p/>
 * Created on: 2012-08-18 08:48:38 <br/>
 *
 * @author Wojciech Niemiec
 * @see pl.bristleback.server.bristle.message.BristleMessage
 */
public final class ActionMessageFactory {

  private ActionMessageFactory() {
    throw new UnsupportedOperationException();
  }

  /**
   * Creates a default action message with action class name and payload defined,
   * that can be then sent using {@link pl.bristleback.server.bristle.message.ConditionObjectSender}.
   *
   * @param actionClass action class name.
   * @param payload     message payload.
   * @param <T>         payload type.
   * @return server action message.
   */
  public static <T> BristleMessage<T> createDefaultMessage(String actionClass, T payload) {
    BristleMessage<T> message = new BristleMessage<T>();
    message.withName(actionClass).withPayload(payload);
    return message;
  }

  /**
   * Creates an action message with action name and payload defined,
   * that can be then sent using {@link pl.bristleback.server.bristle.message.ConditionObjectSender}.
   *
   * @param actionClass action class name.
   * @param action      ation name.
   * @param payload     message payload.
   * @param <T>         payload type.
   * @return server action message.
   */
  public static <T> BristleMessage<T> createMessage(String actionClass, String action, T payload) {
    BristleMessage<T> message = new BristleMessage<T>();
    message.withName(actionClass + StringUtils.DOT_AS_STRING + action).withPayload(payload);
    return message;
  }
}
