/*
 * Bristleback Websocket Framework - Copyright (c) 2010-2013 http://bristleback.pl
 * ---------------------------------------------------------------------------
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation; either version 3 of the License, or (at your
 * option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but without any warranty; without even the implied warranty of merchantability
 * or fitness for a particular purpose.
 * You should have received a copy of the GNU Lesser General Public License along
 * with this program; if not, see <http://www.gnu.org/licenses/lgpl.html>.
 * ---------------------------------------------------------------------------
 */

package pl.bristleback.server.bristle.action;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.action.client.ClientActionsInitializer;
import pl.bristleback.server.bristle.action.exception.handler.ActionExceptionHandlers;
import pl.bristleback.server.bristle.api.BristlebackConfig;
import pl.bristleback.server.bristle.api.DataController;
import pl.bristleback.server.bristle.api.SerializationEngine;
import pl.bristleback.server.bristle.api.users.UserContext;
import pl.bristleback.server.bristle.conf.resolver.action.BristleMessageSerializationUtils;
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
  private BristleMessageSerializationUtils serializationHelper;

  @Inject
  private ClientActionsInitializer clientActionsInitializer;

  private Object messageSerialization;

  private SerializationEngine serializationEngine;

  @Override
  public void init(BristlebackConfig configuration) {
    this.serializationEngine = configuration.getSerializationEngine();

    messageSerialization = serializationEngine.getSerializationResolver()
      .resolveSerialization(serializationHelper.getSerializedArrayMessageType());

    exceptionHandlers.initHandlers();

    clientActionsInitializer.initActionClasses();
  }

  @Override
  @SuppressWarnings("unchecked")
  public void processTextData(String textData, UserContext userContext) {
    ActionExecutionContext context = new ActionExecutionContext(userContext);
    try {
      log.debug("Incoming message: " + textData);
      BristleMessage<String[]> actionMessage = (BristleMessage<String[]>) serializationEngine.deserialize(textData, messageSerialization);
      context.setMessage(actionMessage);
      dispatcher.dispatch(context);
    } catch (Exception e) {
      log.error("Cannot process text data, exception occurred, stage: " + context.getStage(), e);
      exceptionHandlers.handleException(e, context);
    }
  }

  @Override
  public void processBinaryData(byte[] binaryData, UserContext userContext) {
    processTextData(new String(binaryData), userContext);
  }
}
