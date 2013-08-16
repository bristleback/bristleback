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

package pl.bristleback.server.bristle.message;

import pl.bristleback.common.serialization.message.BristleMessage;
import pl.bristleback.server.bristle.api.BristlebackConfig;
import pl.bristleback.server.bristle.api.MessageDispatcher;
import pl.bristleback.server.bristle.api.SerializationEngine;
import pl.bristleback.server.bristle.api.SerializationResolver;
import pl.bristleback.server.bristle.api.WebsocketConnector;
import pl.bristleback.server.bristle.api.WebsocketMessage;
import pl.bristleback.server.bristle.api.action.SendCondition;
import pl.bristleback.server.bristle.api.users.UserContext;
import pl.bristleback.server.bristle.security.UsersContainer;
import pl.bristleback.server.bristle.serialization.RawMessageSerializationEngine;
import pl.bristleback.server.bristle.serialization.SerializationBundle;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

/**
 * @author Pawel Machowski
 * @author Wojciech Niemiec
 *         created at 03.05.12 16:06
 */
public class ConditionObjectSender {

  private MessageDispatcher messageDispatcher;

  private SerializationResolver serializationResolver;

  private SerializationEngine serializationEngine;

  private SerializationBundle localSerializations;

  private SerializationBundle globalDefaultSerializations;

  private UsersContainer connectedUsers;

  private RawMessageSerializationEngine rawMessageSerializationEngine;

  private Field field;

  public void init(BristlebackConfig configuration, UsersContainer usersContainer) {
    this.connectedUsers = usersContainer;
    messageDispatcher = configuration.getMessageConfiguration().getMessageDispatcher();
    serializationEngine = configuration.getSerializationEngine();
    serializationResolver = serializationEngine.getSerializationResolver();
    globalDefaultSerializations = new SerializationBundle();
    rawMessageSerializationEngine = configuration.getComponentsContainer().getFrameworkBean("rawMessageSerializationEngine", RawMessageSerializationEngine.class);
  }

  /**
   * Sends a message with serialization information and list of recipients provided.
   *
   * @param message              message to be sent.
   * @param payloadSerialization payload serialization information.
   * @param connectors           list of recipients.
   * @throws Exception serialization exceptions.
   */
  public void sendMessage(BristleMessage message, Object payloadSerialization, List<WebsocketConnector> connectors) throws Exception {
    WebsocketMessage websocketMessage = serializeToWebSocketMessage(message, payloadSerialization, connectors);
    queueNewMessage(websocketMessage);
  }

  /**
   * Sends a message using default serialization information.
   * Recipients are determined by evaluating
   * {@link pl.bristleback.server.bristle.api.action.SendCondition SendCondition} object.
   *
   * @param message       message to be sent.
   * @param sendCondition condition object used to determine recipients.
   * @throws Exception serialization exceptions.
   */
  public void sendMessage(BristleMessage message, SendCondition sendCondition) throws Exception {
    List<WebsocketConnector> connectors = connectedUsers().getConnectorsByCondition(sendCondition);
    doSendUsingSerialization(message, connectors);
  }

  /**
   * Sends a message using default serialization information to specified recipients.
   *
   * @param message    message to be sent.
   * @param recipients list of recipients.
   * @throws Exception serialization exceptions.
   */
  public void sendMessage(BristleMessage message, List<UserContext> recipients) throws Exception {
    List<WebsocketConnector> connectors = connectedUsers().getConnectorsByUsers(recipients);
    doSendUsingSerialization(message, connectors);
  }

  /**
   * Sends a message using default serialization information.
   * Recipients are determined by evaluating
   * {@link pl.bristleback.server.bristle.api.action.SendCondition SendCondition} object,
   * where the initial pool is passed as the parameter.
   *
   * @param message       message to be sent.
   * @param sendCondition condition object used to determine recipients.
   * @param recipients    initial pool of recipients, for further processing by condition object.
   * @throws Exception serialization exceptions.
   */
  public void sendMessage(BristleMessage message, SendCondition sendCondition, List<UserContext> recipients) throws Exception {
    List<WebsocketConnector> connectors = connectedUsers().getConnectorsByCondition(recipients, sendCondition);
    doSendUsingSerialization(message, connectors);
  }

  /**
   * Sends a connection closing message to given user.
   *
   * @param user user to which the connection should be closed.
   */
  public void closeConnection(UserContext user) {
    List<WebsocketConnector> connectors = connectedUsers().getConnectorsByUsers(Collections.singletonList(user));
    closeConnectionsInServerEngine(connectors);
  }

  /**
   * Sends a connection closing message to all users determined by evaluating passed condition object.
   *
   * @param sendCondition condition object used to determine list of users to which the connection should be closed.
   */
  public void closeConnections(SendCondition sendCondition) {
    List<WebsocketConnector> connectors = connectedUsers().getConnectorsByCondition(sendCondition);
    closeConnectionsInServerEngine(connectors);
  }

  /**
   * Sends a connection closing message to the list of users.
   *
   * @param users users to which the connection should be closed.
   */
  public void closeConnections(List<UserContext> users) {
    List<WebsocketConnector> connectors = connectedUsers().getConnectorsByUsers(users);
    closeConnectionsInServerEngine(connectors);
  }

  private void closeConnectionsInServerEngine(List<WebsocketConnector> connectors) {
    for (WebsocketConnector connector : connectors) {
      connector.stop();
    }
  }

  @SuppressWarnings("unchecked")
  private void doSendUsingSerialization(BristleMessage message, List<WebsocketConnector> connectors) throws Exception {
    Object payloadSerialization;
    Class payloadType;
    if (message.getPayload() != null) {
      payloadType = message.getPayload().getClass();
    } else {
      payloadType = String.class;
    }
    if (localSerializations.isSerializationForPayloadTypeExist(payloadType)) {
      payloadSerialization = localSerializations.getSerialization(payloadType);
    } else if (globalDefaultSerializations.isSerializationForPayloadTypeExist(payloadType)) {
      payloadSerialization = globalDefaultSerializations.getSerialization(payloadType);
    } else {
      payloadSerialization = serializationResolver.resolveSerialization(payloadType);
      globalDefaultSerializations.addSerialization(payloadType, payloadSerialization);
    }

    WebsocketMessage websocketMessage = serializeToWebSocketMessage(message, payloadSerialization, connectors);
    queueNewMessage(websocketMessage);
  }

  public void queueNewMessage(WebsocketMessage websocketMessage) {
    messageDispatcher.addMessage(websocketMessage);
  }

  @SuppressWarnings("unchecked")
  private WebsocketMessage serializeToWebSocketMessage(BristleMessage message, Object payloadSerialization, List<WebsocketConnector> connectors) throws Exception {
    WebsocketMessage<String> websocketMessage = new BaseMessage<String>(MessageType.TEXT);
    String serializedPayload = serializationEngine.serialize(message.getPayload(), payloadSerialization);
    String serializedMessage = rawMessageSerializationEngine.serialize(message.getId(), message.getName(), serializedPayload);
    websocketMessage.setContent(serializedMessage);
    websocketMessage.setRecipients(connectors);
    return websocketMessage;
  }

  private UsersContainer connectedUsers() {
    return connectedUsers;
  }

  public SerializationBundle getLocalSerializations() {
    return localSerializations;
  }

  public void setLocalSerializations(SerializationBundle localSerializations) {
    this.localSerializations = localSerializations;
  }

  public Field getField() {
    return field;
  }

  public void setField(Field field) {
    this.field = field;
  }

}
