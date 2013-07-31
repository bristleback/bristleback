package pl.bristleback.client.actions;

import pl.bristleback.client.serialization.ClientActionHandlers;
import pl.bristleback.client.api.onmessage.MessageHandler;
import pl.bristleback.client.api.onmessage.OnMessageCallback;
import pl.bristleback.client.serialization.FromJsonDeserializer;
import pl.bristleback.common.serialization.message.BristleMessage;

/**
 * <p/>
 * Created on: 30.07.13 20:35 <br/>
 *
 * @author Pawel Machowski
 */
public class ActionController implements OnMessageCallback{

  private ClientActionHandlers clientActionHandlers = new ClientActionHandlers();
  private FromJsonDeserializer fromJsonDeserializer = new FromJsonDeserializer();


  public <T> void registerHandler(String actionClass, String actionMethod, MessageHandler<T> handler) {
   clientActionHandlers.registerHandler(actionClass + actionMethod, handler);
  }

  @Override
  public void onMessage(String payload) {
    BristleMessage bristleMessage = (BristleMessage) fromJsonDeserializer.jsonToObject(payload, BristleMessage.class);
    clientActionHandlers.
    //To change body of implemented methods use File | Settings | File Templates.
  }



/*  Bristleback.controller.ActionMessage = function (controller, message) {
    var messageElements = message.name.split(":");
    var actionElements = messageElements[0].split(".");
    var actionClassName = actionElements[0];
    var actionName = actionElements[1] ? actionElements[1] : "";
    if (message.id) {
      this.actionClass = controller.actionClasses[actionClassName];
    } else {
      this.actionClass = controller.clientActionClasses[actionClassName];
    }

    if (this.actionClass == undefined) {
      var errorMsg = "[ERROR] Cannot find a client action class \"" + actionClassName + "\"";
      Bristleback.Console.log(errorMsg);
      throw new Error(errorMsg);
    }

    this.action = this.actionClass.actions[actionName];
    if (this.action == undefined) {
      errorMsg = "[ERROR] Cannot find action " + (actionName ? "\"" + actionName + "\"" : "default action ") + " in action class \"" + actionClassName + "\"";
      Bristleback.Console.log(errorMsg);
      throw new Error(errorMsg);
    }

    this.callback = controller.callbacks[message.id];
    this.content = message.payload;
    this.exceptionType = messageElements.length > 1 ? this.content.type : undefined;
  };*/
}
