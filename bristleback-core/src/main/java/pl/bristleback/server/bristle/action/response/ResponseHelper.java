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

package pl.bristleback.server.bristle.action.response;

import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.action.ActionExecutionContext;
import pl.bristleback.server.bristle.api.BristlebackConfig;
import pl.bristleback.server.bristle.api.annotations.ObjectSender;
import pl.bristleback.server.bristle.conf.resolver.SpringConfigurationResolver;
import pl.bristleback.server.bristle.message.BristleMessage;
import pl.bristleback.server.bristle.message.ConditionObjectSender;
import pl.bristleback.server.bristle.security.UsersContainer;
import pl.bristleback.server.bristle.serialization.SerializationBundle;
import pl.bristleback.server.bristle.utils.StringUtils;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Collections;

/**
 * This helper class is responsible for sending normal and exception action responses to client.
 * <p/>
 * Created on: 2012-02-05 14:34:55 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component
public class ResponseHelper {

  private static final String EXCEPTION_RESPONSE_SIGN = "exc";

  @Inject
  @Named(SpringConfigurationResolver.CONFIG_BEAN_NAME)
  private BristlebackConfig configuration;

  @Inject
  private UsersContainer connectedUsers;

  @ObjectSender
  private ConditionObjectSender conditionObjectSender;

  @PostConstruct
  private void init() {
    conditionObjectSender = new ConditionObjectSender();
    conditionObjectSender.init(configuration, connectedUsers);
    conditionObjectSender.setLocalSerializations(new SerializationBundle());
  }

  /**
   * Sends normal response to client. This method creates new {@link BristleMessage} object,
   * which is then serialized and sent using {@link ConditionObjectSender} component.
   *
   * @param response response object returned by action.
   * @param context  currently invoked action context.
   * @throws Exception if there is any problem with response serialization.
   */
  public void sendResponse(Object response, ActionExecutionContext context) throws Exception {
    if (context.isResponseSendingCancelled()) {
      return;
    }
    BristleMessage<Object> responseMessage = prepareMessage(response, context);
    Object serialization = context.getAction().getResponseInformation().getSerialization();
    conditionObjectSender.sendMessage(responseMessage, serialization, connectedUsers.getConnectorsByUsers(Collections.singletonList(context.getUserContext())));
  }

  /**
   * Sends exception response to client. This method creates new {@link BristleMessage} object,
   * which is then serialized and sent using {@link ConditionObjectSender} component.
   * Message name has additional <strong>:exc</strong> part,
   * which is correctly parsed by Bristleback as the exception on the client side.
   *
   * @param exceptionResponse exception response thrown/returned by action.
   * @param context           currently invoked action context.
   * @throws Exception if there is any problem with response serialization.
   */
  public void sendExceptionResponse(Object exceptionResponse, ActionExecutionContext context) throws Exception {
    if (exceptionResponse != null) {
      BristleMessage<Object> responseMessage = prepareMessage(exceptionResponse, context);
      conditionObjectSender.sendMessage(responseMessage, Collections.singletonList(context.getUserContext()));
    }
  }

  private BristleMessage<Object> prepareMessage(Object response, ActionExecutionContext context) {
    BristleMessage<Object> responseMessage = new BristleMessage<Object>();
    String messageName = createMessageName(response, context);
    responseMessage
      .withId(context.getMessage().getId())
      .withName(messageName)
      .withPayload(response);
    return responseMessage;
  }

  private String createMessageName(Object response, ActionExecutionContext context) {
    if (response instanceof ExceptionResponse) {
      return context.getMessage().getName() + StringUtils.COLON + EXCEPTION_RESPONSE_SIGN;
    }
    return context.getMessage().getName();
  }

}

