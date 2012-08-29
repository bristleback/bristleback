package pl.bristleback.server.bristle.message;

import org.apache.log4j.Logger;
import pl.bristleback.server.bristle.api.BristlebackConfig;
import pl.bristleback.server.bristle.api.MessageDispatcher;
import pl.bristleback.server.bristle.api.SerializationEngine;
import pl.bristleback.server.bristle.api.ServerEngine;
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
  private Logger log = Logger.getLogger(ConditionObjectSender.class.getName());

  private MessageDispatcher messageDispatcher;

  private SerializationAnnotationResolver serializationResolver;
  private SerializationEngine serializationEngine;

  private SerializationBundle serializationBundle;

  private UsersContainer connectedUsers;

  private ServerEngine serverEngine;

  public void init(BristlebackConfig configuration, UsersContainer usersContainer) {
    this.connectedUsers = usersContainer;
    this.serverEngine = configuration.getServerEngine();
    messageDispatcher = configuration.getMessageConfiguration().getMessageDispatcher();
    serializationEngine = configuration.getSerializationEngine();
    serializationResolver = configuration.getSpringIntegration().getFrameworkBean("serializationAnnotationResolver", SerializationAnnotationResolver.class);
  }

  public void sendMessage(BristleMessage message, Object serialization, List<IdentifiedUser> users) throws Exception {
    List<WebsocketConnector> connectors = connectedUsers().getConnectorsByUsers(users);
    WebsocketMessage websocketMessage = serializeToWebSocketMessage(message, serialization, connectors);
    queueNewMessage(websocketMessage);
  }

  public void sendMessage(BristleMessage message, SendCondition sendCondition) throws Exception {
    List<WebsocketConnector> connectors = connectedUsers().getConnectorsByCondition(sendCondition);
    doSendUsingDefaultSerialization(message, connectors);
  }

  public void sendMessage(BristleMessage message, List<IdentifiedUser> users) throws Exception {
    List<WebsocketConnector> connectors = connectedUsers().getConnectorsByUsers(users);
    doSendUsingDefaultSerialization(message, connectors);
  }

  public void sendMessage(BristleMessage message, SendCondition sendCondition, List<IdentifiedUser> users) throws Exception {
    List<WebsocketConnector> connectors = connectedUsers().getConnectorsByCondition(users, sendCondition);
    doSendUsingDefaultSerialization(message, connectors);
  }

  public void sendNamedMessage(BristleMessage message, String serializationName, SendCondition sendCondition) throws Exception {
    List<WebsocketConnector> connectors = connectedUsers().getConnectorsByCondition(sendCondition);
    doSendUsingSerialization(message, serializationName, connectors);
  }

  public void sendNamedMessage(BristleMessage message, String serializationName, List<IdentifiedUser> users) throws Exception {
    List<WebsocketConnector> connectors = connectedUsers().getConnectorsByUsers(users);
    doSendUsingSerialization(message, serializationName, connectors);
  }

  public void sendNamedMessage(BristleMessage message, String serializationName, SendCondition sendCondition, List<IdentifiedUser> users) throws Exception {
    List<WebsocketConnector> connectors = connectedUsers().getConnectorsByCondition(users, sendCondition);
    doSendUsingSerialization(message, serializationName, connectors);
  }

  public void closeConnection(IdentifiedUser user) {
    List<WebsocketConnector> connectors = connectedUsers().getConnectorsByUsers(Collections.singletonList(user));
    closeConnectionsInServerEngine(connectors);
  }

  public void closeConnections(SendCondition sendCondition) {
    List<WebsocketConnector> connectors = connectedUsers().getConnectorsByCondition(sendCondition);
    closeConnectionsInServerEngine(connectors);
  }

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
