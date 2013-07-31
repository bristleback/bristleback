package pl.bristleback.client.actions;

import pl.bristleback.client.api.onmessage.MessageHandler;
import pl.bristleback.common.serialization.message.BristleMessage;

import java.util.HashMap;
import java.util.Map;

/**
 * <p/>
 * Created on: 30.07.13 20:27 <br/>
 *
 * @author Pawel Machowski
 */
public class ClientActionHandlers {

  private Map<String, MessageHandler> handlers = new HashMap<String, MessageHandler>();


  public <T> void registerHandler(String actionFullName, MessageHandler<T> handler) {
    handlers.put(actionFullName, handler);
  }

  protected void onServerEvent(BristleMessage<String[]> message) {
    MessageHandler messageHandler = handlers.get(message.getName());
    messageHandler.onMessage(message.getPayload());
  }

}
