package pl.bristleback.performance;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketClient;

import java.io.IOException;
import java.net.URI;

public class TestWebsocketClient implements WebSocket.OnTextMessage {

  private static final String URL_FORMAT = "ws://%s:%s/websocket";

  private final WebSocket.Connection connection;
  private TestContext testContext;

  public TestWebsocketClient(WebSocketClient websocketClient, TestConfiguration configuration, TestContext testContext) throws Exception {
    this.testContext = testContext;
    URI uri = new URI(String.format(URL_FORMAT, configuration.getHost(), configuration.getPort()));
    connection = websocketClient.open(uri, this).get();
  }

  public void send(String message) throws IOException {
    connection.sendMessage(message);
    testContext.messageSent();
  }

  public void disconnect() throws IOException {
    connection.close();
  }

  @Override
  public void onMessage(String data) {
    testContext.messageReceived();
  }

  @Override
  public void onOpen(Connection connection) {
    testContext.clientConnected();
  }

  @Override
  public void onClose(int closeCode, String message) {
    testContext.clientDisconnected();
  }
}
