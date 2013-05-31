package pl.bristleback.client;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketClient;
import org.eclipse.jetty.websocket.WebSocketClientFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.bristleback.client.api.BristlebackClient;
import pl.bristleback.client.connection.ServerUrl;
import pl.bristleback.client.exceptions.ConnectionException;
import pl.bristleback.client.exceptions.MessageSendingException;
import pl.bristleback.client.api.onmessage.OnMessageCallback;
import pl.bristleback.client.serialization.BristleMessage;
import pl.bristleback.client.serialization.ToJsonSerializer;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * <p/>
 * Created on: 5/31/13 12:46 PM <br/>
 *
 * @author Pawel Machowski
 */
public class JettyClient implements BristlebackClient, WebSocket.OnTextMessage {
  private static final Logger LOGGER = LoggerFactory.getLogger(JettyClient.class);

  private Connection connection;

  public ToJsonSerializer serializer = new ToJsonSerializer();
  private ServerUrl serverUrl = new ServerUrl();
  private OnMessageCallback onMessageCallback;
  private Future<Connection> connectionFuture;
  private final WebSocketClientFactory webSocketClientFactory = new WebSocketClientFactory();

  public JettyClient(OnMessageCallback onMessageCallback) {
    this.onMessageCallback = onMessageCallback;
  }

  public JettyClient(OnMessageCallback onMessageCallback, String fullUrl) {
    this.onMessageCallback = onMessageCallback;
    serverUrl.setFullUrl(fullUrl);
  }

  public void onMessage(String serverMessage) {
    onMessageCallback.onMessage(serverMessage);
  }

  public void onOpen(Connection connection) {
    LOGGER.info("Connection to server {} is now opened", serverUrl.resolveUrl());
  }

  public void onClose(int i, String s) {
    LOGGER.info("Connection to server {} is now closed", serverUrl.resolveUrl());
  }

  public void connect() {
    try {
      webSocketClientFactory.start();
      WebSocketClient webSocketClient = webSocketClientFactory.newWebSocketClient();
      connectionFuture = webSocketClient.open(serverUrl.resolveUrl(), this);
    } catch (Exception e) {
      //TODO handle
      throw new ConnectionException(e);
    }
  }

  private Connection getConnection() {
    if (connection == null) {
      try {
        //todo check if you can call future.get multiple times, if yes that method and field connection can be removed
        connection = connectionFuture.get();
      } catch (InterruptedException e) {
        throw new ConnectionException(e);
      } catch (ExecutionException e) {
        throw new ConnectionException(e);
      }
    }
    return connection;
  }

  public void disconnect() {
    getConnection().close();
    try {
      webSocketClientFactory.stop();
    } catch (Exception e) {
      throw new ConnectionException(e);
    }
  }

  public void sendMessage(BristleMessage bristleMessage) {
    try {
      bristleMessage.withId(Math.random() + "");

      String jsonPayload = serializer.objectToJson(bristleMessage);
      getConnection().sendMessage(jsonPayload);
    } catch (IOException e) {
      //TODO handle
      throw new MessageSendingException(e);
    }
  }

}
