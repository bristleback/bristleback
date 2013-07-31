package pl.bristleback.client.connection;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketClient;
import org.eclipse.jetty.websocket.WebSocketClientFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.bristleback.client.JettyClient;
import pl.bristleback.client.api.onmessage.OnMessageCallback;
import pl.bristleback.client.exceptions.ConnectionException;
import pl.bristleback.client.exceptions.MessageSendingException;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * <p/>
 * Created on: 31.07.13 20:00 <br/>
 *
 * @author Pawel Machowski
 */
public class WebSocketConnection implements WebSocket.OnTextMessage {
  private static final Logger LOGGER = LoggerFactory.getLogger(JettyClient.class);

  private ServerUrl serverUrl = new ServerUrl();
  private final WebSocketClientFactory webSocketClientFactory = new WebSocketClientFactory();
  private Future<Connection> connectionFuture;
  private Connection connection;
  private OnMessageCallback onMessageCallback;


  public WebSocketConnection(ServerUrl serverUrl, OnMessageCallback onMessageCallback) {
    this.serverUrl = serverUrl;
    this.onMessageCallback = onMessageCallback;
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

  private WebSocket.Connection getConnection() {
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

  public void sendMessage(String jsonPayload) {
    try {
      getConnection().sendMessage(jsonPayload);
    } catch (IOException e) {
      //TODO handle
      throw new MessageSendingException(e);
    }
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


}
