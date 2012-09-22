package pl.bristleback.server.bristle.message;

import pl.bristleback.server.bristle.api.BristlebackConfig;
import pl.bristleback.server.bristle.api.MessageDispatcher;
import pl.bristleback.server.bristle.api.SerializationEngine;
import pl.bristleback.server.bristle.api.WebsocketConnector;
import pl.bristleback.server.bristle.api.WebsocketMessage;
import pl.bristleback.server.bristle.api.action.SendCondition;
import pl.bristleback.server.bristle.api.users.IdentifiedUser;
import pl.bristleback.server.bristle.authorisation.user.UsersContainer;
import pl.bristleback.server.bristle.conf.resolver.serialization.SerializationAnnotationResolver;
import pl.bristleback.server.bristle.exceptions.SerializationResolvingException;
import pl.bristleback.server.bristle.serialization.MessageType;
import pl.bristleback.server.bristle.serialization.SerializationBundle;

import java.util.Collections;
import java.util.List;

/**
 * @author Pawel Machowski
 * @author Wojciech Niemiec
 *         created at 03.05.12 16:06
 */
public class ConditionObjectSender {

  private MessageDispatcher messageDispatcher;

  private SerializationAnnotationResolver serializationResolver;
  private SerializationEngine serializationEngine;

  private SerializationBundle serializationBundle;

  private UsersContainer connectedUsers;

  public void init(BristlebackConfig configuration, UsersContainer usersContainer) {
    this.connectedUsers = usersContainer;
    messageDispatcher = configuration.getMessageConfiguration().getMessageDispatcher();
    serializationEngine = configuration.getSerializationEngine();
    serializationResolver = configuration.getSpringIntegration().getFrameworkBean("serializationAnnotationResolver", SerializationAnnotationResolver.class);
  }

  /**
   * Sends a message with serialization information and list of recipients provided.
   *
   * @param message       message to be sent.
   * @param serialization serialization information.
   * @param recipients    list of recipients.
   * @throws Exception serialization exceptions.
   */
  public void sendMessage(BristleMessage message, Object serialization, List<IdentifiedUser> recipients) throws Exception {
    List<WebsocketConnector> connectors = connectedUsers().getConnectorsByUsers(recipients);
    WebsocketMessage websocketMessage = serializeToWebSocketMessage(message, serialization, connectors);
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
    doSendUsingDefaultSerialization(message, connectors);
  }

  /**
   * Sends a message using default serialization information to specified recipients.
   *
   * @param message    message to be sent.
   * @param recipients list of recipients.
   * @throws Exception serialization exceptions.
   */
  public void sendMessage(BristleMessage message, List<IdentifiedUser> recipients) throws Exception {
    List<WebsocketConnector> connectors = connectedUsers().getConnectorsByUsers(recipients);
    doSendUsingDefaultSerialization(message, connectors);
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
  public void sendMessage(BristleMessage message, SendCondition sendCondition, List<IdentifiedUser> recipients) throws Exception {
    List<WebsocketConnector> connectors = connectedUsers().getConnectorsByCondition(recipients, sendCondition);
    doSendUsingDefaultSerialization(message, connectors);
  }

  /**
   * Sends a message using serialization information with provided name.
   * Recipients are determined by evaluating
   * {@link pl.bristleback.server.bristle.api.action.SendCondition SendCondition} object.
   *
   * @param message           message to be sent.
   * @param serializationName non default serialization information name.
   * @param sendCondition     condition object used to determine recipients.
   * @throws Exception serialization exceptions.
   * @see pl.bristleback.server.bristle.api.annotations.Serialize
   */
  public void sendNamedMessage(BristleMessage message, String serializationName, SendCondition sendCondition) throws Exception {
    List<WebsocketConnector> connectors = connectedUsers().getConnectorsByCondition(sendCondition);
    doSendUsingSerialization(message, serializationName, connectors);
  }

  /**
   * Sends a message to the list of recipients using serialization information with provided name.
   *
   * @param message           message to be sent.
   * @param serializationName non default serialization information name.
   * @param recipients        list of recipients.
   * @throws Exception serialization exceptions.
   * @see pl.bristleback.server.bristle.api.annotations.Serialize
   */
  public void sendNamedMessage(BristleMessage message, String serializationName, List<IdentifiedUser> recipients) throws Exception {
    List<WebsocketConnector> connectors = connectedUsers().getConnectorsByUsers(recipients);
    doSendUsingSerialization(message, serializationName, connectors);
  }

  /**
   * Sends a message using serialization information with provided name.
   * Recipients are determined by evaluating
   * {@link pl.bristleback.server.bristle.api.action.SendCondition SendCondition} object,
   * where the initial pool is passed as the parameter.
   *
   * @param message           message to be sent.
   * @param serializationName non default serialization information name.
   * @param sendCondition     condition object used to determine recipients.
   * @param recipients        initial pool of recipients, for further processing by condition object.
   * @throws Exception serialization exceptions.
   * @see pl.bristleback.server.bristle.api.annotations.Serialize
   */
  public void sendNamedMessage(BristleMessage message, String serializationName, SendCondition sendCondition, List<IdentifiedUser> recipients) throws Exception {
    List<WebsocketConnector> connectors = connectedUsers().getConnectorsByCondition(recipients, sendCondition);
    doSendUsingSerialization(message, serializationName, connectors);
  }

  /**
   * Sends a connection closing message to given user.
   *
   * @param user user to which the connection should be closed.
   */
  public void closeConnection(IdentifiedUser user) {
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
  public void closeConnections(List<IdentifiedUser> users) {
    List<WebsocketConnector> connectors = connectedUsers().getConnectorsByUsers(users);
    closeConnectionsInServerEngine(connectors);
  }

  private void closeConnectionsInServerEngine(List<WebsocketConnector> connectors) {
    for (WebsocketConnector connector : connectors) {
      connector.stop();
    }
  }

  private void doSendUsingDefaultSerialization(BristleMessage message, List<WebsocketConnector> connectors) throws Exception {
    Object serialization = getDefaultSerialization(message);
    WebsocketMessage websocketMessage = serializeToWebSocketMessage(message, serialization, connectors);
    queueNewMessage(websocketMessage);
  }

  private void doSendUsingSerialization(BristleMessage message, String serializationName, List<WebsocketConnector> connectors) throws Exception {
    Object serialization = getSerialization(serializationName);
    WebsocketMessage websocketMessage = serializeToWebSocketMessage(message, serialization, connectors);
    queueNewMessage(websocketMessage);
  }

  private Object getSerialization(String serializationName) {
    Object serialization = serializationBundle.getSerialization(serializationName);
    if (serialization == null) {
      throw new SerializationResolvingException("Cannot find serialization " + serializationName);
    }
    return serialization;
  }

  private Object getDefaultSerialization(BristleMessage message) {
    if (serializationBundle != null && serializationBundle.containsDefaultSerialization()) {
      return serializationBundle.getDefaultSerialization();
    }
    return serializationResolver.resolveDefaultSerialization(message.getPayload().getClass());
  }

  private void queueNewMessage(WebsocketMessage websocketMessage) {
    messageDispatcher.addMessage(websocketMessage);
  }

  @SuppressWarnings("unchecked")
  private WebsocketMessage serializeToWebSocketMessage(BristleMessage message, Object serialization, List<WebsocketConnector> connectors) throws Exception {
    WebsocketMessage<String> websocketMessage = new BaseMessage<String>(MessageType.TEXT);
    String serializedMessage = serializationEngine.serialize(message, serialization);
    websocketMessage.setContent(serializedMessage);
    websocketMessage.setRecipients(connectors);
    return websocketMessage;
  }

  private UsersContainer connectedUsers() {
    return connectedUsers;
  }

  public SerializationBundle getSerializationBundle() {
    return serializationBundle;
  }

  public void setSerializationBundle(SerializationBundle serializationBundle) {
    this.serializationBundle = serializationBundle;
  }
}
