package pl.bristleback.performance;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketClient;
import org.eclipse.jetty.websocket.WebSocketClientFactory;

import java.io.IOException;
import java.net.URI;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicLong;


/* ------------------------------------------------------------ */

/**
 * WebSocket Example Chat client. It creates a number of WebSocket chat connections and then picks random
 * connections to send messages on.   The RECEIVED messages are simply counted.
 */
public class ChatLoadClient implements WebSocket.OnTextMessage {

  private static final AtomicLong SENT = new AtomicLong(0);
  private static final AtomicLong RECEIVED = new AtomicLong(0);
  private static final Set<ChatLoadClient> MEMBERS = new CopyOnWriteArraySet<ChatLoadClient>();
  private final Connection connection;

  private static String host = "localhost";
  private static int port = 8765;
  private static int clientNumber = 10;
  private static int messagesNumber = 10000;

  /* ------------------------------------------------------------ */

  /**
   * Construct a Chat Load Client
   *
   * @param client The WebSocketClient to use for the connection.
   * @param host   The host to connect to
   * @param port   The port to connect to
   * @throws Exception
   */
  public ChatLoadClient(WebSocketClient client, String host, int port)
    throws Exception {
    URI uri = new URI("ws://" + host + ":" + port + "/websocket");
    connection = client.open(uri, this).get();
  }

  /* ------------------------------------------------------------ */

  /**
   * Send a chat message from the user
   *
   * @param message the message to send
   * @throws IOException
   */
  public void send(String message) throws IOException {
    connection.sendMessage(message);
    SENT.incrementAndGet();
  }

  /* ------------------------------------------------------------ */

  /**
   * Callback on successful open of the websocket connection.
   */
  public void onOpen(Connection connection) {
    MEMBERS.add(this);
  }

  /* ------------------------------------------------------------ */

  /**
   * Callback on close of the WebSocket connection
   */
  public void onClose(int closeCode, String message) {
    MEMBERS.remove(this);
  }

  /* ------------------------------------------------------------ */

  /**
   * Callback on receiving a message
   */
  public void onMessage(String data) {
    RECEIVED.incrementAndGet();
  }

  /* ------------------------------------------------------------ */

  /**
   * Disconnect the client
   *
   * @throws IOException
   */
  public void disconnect() throws IOException {
    connection.close();
  }

  /* ------------------------------------------------------------ */

  /**
   * Main method to create and use ChatLoadClient instances. <p>The default is to connection to localhost:8080 with 1000
   * clients and to send 1000 messages.
   *
   * @param arg The command line arguments are [ host [ port [ clients [ messages ]]]]. The default is to connection to
   *            localhost:8080 with 1000 clients and to send 1000 messages.
   * @throws Exception
   */
  public static void main(String... arg) throws Exception {

    host = arg.length > 0 ? arg[0] : "localhost";
    port = arg.length > 1 ? Integer.parseInt(arg[1]) : 8765;
    clientNumber = arg.length > 2 ? Integer.parseInt(arg[2]) : 10;
    messagesNumber = arg.length > 3 ? Integer.parseInt(arg[3]) : 10000;

    long testResult = runTests();
    System.out.println("TEST RESULT: " + testResult);


  }

  private static long runTests() throws Exception {
    WebSocketClientFactory clientFactory = new WebSocketClientFactory();
    WebSocketClient webSocketClient = clientFactory.newWebSocketClient();
    webSocketClient.setMaxIdleTime(300000);
    webSocketClient.setProtocol("");
    clientFactory.start();

    //FIRST TEST!
    ChatLoadClient[] chatClients = testOpenConnection(webSocketClient);

    // Send messages
    Random random = new Random();
    long start = System.currentTimeMillis();

    int clientIndex = 0;
    for (int i = 0; i < messagesNumber; i++) {
      if (clientIndex == chatClients.length - 1) {
        clientIndex = 0;
      }
      ChatLoadClient chatClient = chatClients[clientIndex];
      String msg = buildBristlebackMessage(random.nextInt());
      chatClient.send(msg);
      clientIndex++;
    }

    long last = 0;
    long progress = start;
    while (RECEIVED.get() < (messagesNumber)) {
      if (System.currentTimeMillis() > (progress + webSocketClient.getMaxIdleTime())) {
        System.out.println("TIMEOUT!");
        break;
      }
      if (RECEIVED.get() != last) {
        progress = System.currentTimeMillis();
        last = RECEIVED.get();
      }
      Thread.sleep(1);
    }
    long end = System.currentTimeMillis();
    long messagesSentCount = (RECEIVED.get() * 1000) / (end - start);
    System.err.printf("Sent/Received %d/%d messages in %dms: %dmsg/s\n", SENT.get(), RECEIVED.get(), (end - start), messagesSentCount);

    // Close all connections
    start = System.currentTimeMillis();
    for (ChatLoadClient chatClient : chatClients) {
      chatClient.disconnect();
    }
    while (MEMBERS.size() > 0) {
      if (System.currentTimeMillis() > (start + webSocketClient.getMaxIdleTime())) {
        break;
      }
      Thread.sleep(10);
    }
    end = System.currentTimeMillis();

    System.err.printf("Closed %d connections to %s:%d in %dms\n", clientNumber, host, port, (end - start));

    clientFactory.stop();
    return messagesSentCount;
  }

  private static ChatLoadClient[] testOpenConnection(WebSocketClient webSocketClient) throws Exception {
    long startTime = System.currentTimeMillis();
    // Create client serially
    ChatLoadClient[] chatClients = createClients(webSocketClient, clientNumber);
    while (MEMBERS.size() < clientNumber) {
      if (System.currentTimeMillis() > (startTime + webSocketClient.getMaxIdleTime())) {
        break;
      }
      Thread.sleep(10);
    }
    long end = System.currentTimeMillis();

    System.err.printf("Opened %d of %d connections to %s:%d in %dms\n", MEMBERS.size(), clientNumber, host, port, (end - startTime));
    return chatClients;
  }

  private static ChatLoadClient[] createClients(WebSocketClient client, int clientsCount) throws Exception {
    ChatLoadClient[] chatClients = new ChatLoadClient[clientsCount];
    for (int i = 0; i < chatClients.length; i++) {
      chatClients[i] = new ChatLoadClient(client, host, port);
    }
    return chatClients;
  }

  private static String buildBristlebackMessage(int randomText) {
    return "{\"name\":\"HelloWorld\",\"payload\":[\"uc\",{\"key\":" + randomText + "}],\"id\":2}";
  }

}
