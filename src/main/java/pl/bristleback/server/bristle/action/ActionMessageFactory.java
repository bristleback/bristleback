package pl.bristleback.server.bristle.action;

import pl.bristleback.server.bristle.message.BristleMessage;
import pl.bristleback.server.bristle.utils.StringUtils;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-08-18 08:48:38 <br/>
 *
 * @author Wojciech Niemiec
 */
public final class ActionMessageFactory {

  private ActionMessageFactory() {
    throw new UnsupportedOperationException();
  }

  public static <T> BristleMessage<T> createDefaultMessage(String actionClass, T payload) {
    BristleMessage<T> message = new BristleMessage<T>();
    message.withName(actionClass).withPayload(payload);
    return message;
  }

  public static <T> BristleMessage<T> createDefaultExceptionMessage(String actionClass, String exceptionType, T payload) {
    BristleMessage<T> message = new BristleMessage<T>();
    message.withName(actionClass + StringUtils.COLON + exceptionType).withPayload(payload);
    return message;
  }

  public static <T> BristleMessage<T> createMessage(String actionClass, String action, T payload) {
    BristleMessage<T> message = new BristleMessage<T>();
    message.withName(actionClass + StringUtils.DOT_AS_STRING + action).withPayload(payload);
    return message;
  }

  public static <T> BristleMessage<T> createExceptionMessage(String actionClass, String action, String exceptionType, T payload) {
    BristleMessage<T> message = new BristleMessage<T>();
    message.withName(actionClass + StringUtils.DOT_AS_STRING + action + StringUtils.COLON + exceptionType).withPayload(payload);
    return message;
  }
}
