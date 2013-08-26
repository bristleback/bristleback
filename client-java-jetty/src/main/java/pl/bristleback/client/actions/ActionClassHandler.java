package pl.bristleback.client.actions;

import pl.bristleback.client.api.onmessage.MessageHandler;

/**
 * <p/>
 * Created on: 11.08.13 13:50 <br/>
 *
 * @author Pawel Machowski
 */
public class ActionClassHandler {

  private ClientActionHandlers clientActionHandlers;
  private String actionClassName;


  public ActionClassHandler(String actionClassName, ClientActionHandlers clientActionHandlers) {
    this.clientActionHandlers = clientActionHandlers;
    this.actionClassName = actionClassName;
  }

  public <T> ActionClassHandler withMethod(String actionMethodName, MessageHandler<T> handler) {
    clientActionHandlers.registerHandler(actionClassName + "." + actionMethodName, handler);

    return this;
  }

}
