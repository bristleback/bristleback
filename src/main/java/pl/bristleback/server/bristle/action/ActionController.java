package pl.bristleback.server.bristle.action;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.action.client.ClientActionsInitializer;
import pl.bristleback.server.bristle.action.exception.handler.ActionExceptionHandlers;
import pl.bristleback.server.bristle.api.BristlebackConfig;
import pl.bristleback.server.bristle.api.DataController;
import pl.bristleback.server.bristle.api.SerializationEngine;
import pl.bristleback.server.bristle.api.users.IdentifiedUser;
import pl.bristleback.server.bristle.conf.resolver.action.BristleMessageSerialization;
import pl.bristleback.server.bristle.message.BristleMessage;

import javax.inject.Inject;

@Component("system.controller.action")
public class ActionController implements DataController {

  private static Logger log = Logger.getLogger(ActionController.class.getName());

  @Inject
  private ActionDispatcher dispatcher;

  @Inject
  private ActionExceptionHandlers exceptionHandlers;

  @Inject
  private BristleMessageSerialization serializationHelper;

  @Inject
  private ClientActionsInitializer clientActionsInitializer;

  private Object messageSerialization;

  private SerializationEngine serializationEngine;

  @Override
  public void init(BristlebackConfig configuration) {
    this.serializationEngine = configuration.getSerializationEngine();

    messageSerialization = serializationEngine.getSerializationResolver()
      .resolveDefaultSerialization(serializationHelper.getSerializedArrayMessageType());

    exceptionHandlers.initHandlers();

    clientActionsInitializer.initActionClasses();
  }

  @Override
  @SuppressWarnings("unchecked")
  public void processTextData(String textData, IdentifiedUser user) {
    ActionExecutionContext context = new ActionExecutionContext(user);
    try {
//      log.debug("Incoming message: " + textData);
      BristleMessage<String[]> actionMessage = (BristleMessage<String[]>) serializationEngine.deserialize(textData, messageSerialization);
      context.setMessage(actionMessage);
      dispatcher.dispatch(context);
    } catch (Exception e) {
      log.error("Cannot process text data, exception occurred, stage: " + context.getStage(), e);
      exceptionHandlers.handleException(e, context);
    }
  }

  @Override
  public void processBinaryData(byte[] binaryData, IdentifiedUser user) {
    processTextData(new String(binaryData), user);
  }
}
