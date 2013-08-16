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

package pl.bristleback.server.bristle.action.client;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import pl.bristleback.server.bristle.api.SerializationEngine;
import pl.bristleback.server.bristle.api.WebsocketConnector;
import pl.bristleback.server.bristle.api.WebsocketMessage;
import pl.bristleback.server.bristle.api.action.ClientActionSender;
import pl.bristleback.server.bristle.conf.BristlebackComponentsContainer;
import pl.bristleback.server.bristle.message.BaseMessage;
import pl.bristleback.server.bristle.message.ConditionObjectSender;
import pl.bristleback.server.bristle.message.MessageType;
import pl.bristleback.server.bristle.serialization.RawMessageSerializationEngine;

import java.util.List;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-05-26 12:07:38 <br/>
 *
 * @author Wojciech Niemiec
 */
public class ClientActionProxyInterceptor implements MethodInterceptor {

  private ClientActionClasses actionClasses;

  private ConditionObjectSender objectSender;

  private SerializationEngine serializationEngine;

  private RawMessageSerializationEngine rawMessageSerializationEngine;

  public void init(BristlebackComponentsContainer componentsContainer, ConditionObjectSender objectsSender) {
    this.actionClasses = componentsContainer.getFrameworkBean("clientActionClasses", ClientActionClasses.class);
    this.serializationEngine = componentsContainer.getFrameworkBean("serializationEngine", SerializationEngine.class);
    this.rawMessageSerializationEngine = componentsContainer.getFrameworkBean("rawMessageSerializationEngine", RawMessageSerializationEngine.class);
    this.objectSender = objectsSender;
  }

  @Override
  @SuppressWarnings("unchecked")
  public Object invoke(MethodInvocation invocation) throws Throwable {
    ClientActionInformation actionInformation = resolveActionInformation(invocation);

    Object methodOutput = invocation.proceed();
    Object[] parameters = invocation.getArguments();

    String[] payload = new String[actionInformation.getPayloadLength()];

    int currentIndex = 0;

    for (int i = 0; i < parameters.length; i++) {
      Object parameter = parameters[i];
      ClientActionParameterInformation parameterInformation = actionInformation.getParameters().get(i);
      if (parameterInformation.isForSerialization()) {
        payload[currentIndex] = serializationEngine.serialize(parameter, parameterInformation.getSerialization());
        currentIndex++;
      }
    }

    String serializedMessage = rawMessageSerializationEngine.serialize(null, actionInformation.getFullName(), payload);
    ClientActionSender clientActionSender = actionInformation.getResponse();
    List<WebsocketConnector> recipients = clientActionSender.chooseRecipients(methodOutput, actionInformation);
    WebsocketMessage<String> message = new BaseMessage<String>(MessageType.TEXT);
    message.setContent(serializedMessage);
    message.setRecipients(recipients);

    objectSender.queueNewMessage(message);
    return methodOutput;
  }

  private ClientActionInformation resolveActionInformation(MethodInvocation invocation) {
    ClientActionClassInformation actionClassInformation = actionClasses.getClientActionClass(invocation.getThis().getClass());
    return actionClassInformation.getClientAction(invocation.getMethod());
  }
}
