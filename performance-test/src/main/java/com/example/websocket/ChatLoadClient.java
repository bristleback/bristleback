package com.example.websocket;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketClient;

import java.io.IOException;
import java.net.URI;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicLong;


/* ------------------------------------------------------------ */

/**
 * WebSocket Example Chat client. <p>This class is a example of the Jetty WebSocket client API to create a load tester
 * for the simple {@link ChatServlet}. It create a number of WebSocket chat connections and then picks random
 * connections to send messages on.   The received messages are simply counted.
 */
public class ChatLoadClient implements WebSocket.OnTextMessage {
  private static final AtomicLong sent = new AtomicLong(0);
  private static final AtomicLong received = new AtomicLong(0);
  private static final Set<ChatLoadClient> members = new CopyOnWriteArraySet<ChatLoadClient>();
  private final String name;
  private final Connection connection;

  private static String host = "localhost";
  private static int port = 8080;


  /* ------------------------------------------------------------ */

  /**
   * Construct a Chat Load Client
   *
   * @param username The username of the client
   * @param client   The WebSocketClient to use for the connection.
   * @param host     The host to connect to
   * @param port     The port to connect to
   * @throws Exception
   */
  public ChatLoadClient(String username, WebSocketClient client, String host, int port)
    throws Exception {
    name = username;
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
    sent.incrementAndGet();
  }

  /* ------------------------------------------------------------ */

  /**
   * Callback on successful open of the websocket connection.
   */
  public void onOpen(Connection connection) {
    members.add(this);
  }

  /* ------------------------------------------------------------ */

  /**
   * Callback on close of the WebSocket connection
   */
  public void onClose(int closeCode, String message) {
    members.remove(this);
  }

  /* ------------------------------------------------------------ */

  /**
   * Callback on receiving a message
   */
  public void onMessage(String data) {
//    System.out.println(data);
    received.incrementAndGet();
  }

  /* ------------------------------------------------------------ */

  /**
   * Disconnect the client
   *
   * @throws IOException
   */
  public void disconnect() throws IOException {
    connection.disconnect();
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
//       String host = arg.length > 0 ? arg[0] : "localhost";
//    int port = arg.length > 1 ? Integer.parseInt(arg[1]) : 8080;
    int mesgs = arg.length > 0 ? Integer.parseInt(arg[3]) : 1000;
    int clients = arg.length > 1 ? Integer.parseInt(arg[2]) : 1000;

    long testResult = runTests(mesgs, clients);
    System.out.println("TEST RESULT: " + testResult);


  }

  private static long runTests(int messagesCount, int clientsCount) throws Exception {
    WebSocketClient webSocketClient = new WebSocketClient();
    webSocketClient.setBufferSize(4096);
    webSocketClient.setMaxIdleTime(30000);
    webSocketClient.setProtocol("");
    webSocketClient.start();

    // Create client serially
    ChatLoadClient[] chatClients = createClients(webSocketClient, clientsCount);

    //FIRST TEST!
    testOpenConnection(webSocketClient, clientsCount);

    // Send messages
    Random random = new Random();
    long start = System.currentTimeMillis();

    int clientIndex = 0;
    for (int i = 0; i < messagesCount; i++) {
      if (clientIndex == chatClients.length - 1) {
        clientIndex = 0;
      }
      ChatLoadClient chatClient = chatClients[clientIndex];
      String msg = buildBristlebackMessage();
      chatClient.send(msg);
      clientIndex++;
    }
//    System.out.println("sent all messages");
    long last = 0;
    long progress = start;
//    System.out.println(received.get() < (clientsCount * MESSAGES_COUNT));
//    System.out.println("clientsCount * MESSAGES_COUNT: " + clientsCount * MESSAGES_COUNT);
    while (received.get() < (messagesCount)) {
//      System.out.println(received.get() < (clientsCount * MESSAGES_COUNT));
      if (System.currentTimeMillis() > (progress + webSocketClient.getMaxIdleTime())) {
        System.out.println("TIMEOUT!");
        break;
      }
      if (received.get() != last) {
        progress = System.currentTimeMillis();
//        System.out.println(progress);
        last = received.get();
      }
//      System.out.println("RECEIVED GET: " + received.get());
      Thread.sleep(1);
    }
//    System.out.println("all messages received");
    long end = System.currentTimeMillis();
    long messagesSentCount = (received.get() * 1000) / (end - start);
    System.err.printf("Sent/Received %d/%d messages in %dms: %dmsg/s\n", sent.get(), received.get(), (end - start), messagesSentCount);

    // Close all connections
    start = System.currentTimeMillis();
    for (int i = 0; i < chatClients.length; i++)
      chatClients[i].disconnect();
    while (members.size() > 0) {
      if (System.currentTimeMillis() > (start + webSocketClient.getMaxIdleTime()))
        break;
      Thread.sleep(10);
    }
    end = System.currentTimeMillis();

    System.err.printf("Closed %d connections to %s:%d in %dms\n", clientsCount, host, port, (end - start));

    webSocketClient.stop();
    return messagesSentCount;
  }

  private static void testOpenConnection(WebSocketClient webSocketClient, int clientsCount) throws InterruptedException {
    long startTime = System.currentTimeMillis();
    while (members.size() < clientsCount) {
      if (System.currentTimeMillis() > (startTime + webSocketClient.getMaxIdleTime())) {
        break;
      }
      Thread.sleep(10);
    }
    long end = System.currentTimeMillis();

    System.err.printf("Opened %d of %d connections to %s:%d in %dms\n", members.size(), clientsCount, host, port, (end - startTime));
  }

  private static ChatLoadClient[] createClients(WebSocketClient client, int clientsCount) throws Exception {
    ChatLoadClient[] chatClients = new ChatLoadClient[clientsCount];
    for (int i = 0; i < chatClients.length; i++) {
      chatClients[i] = new ChatLoadClient("user" + i, client, host, port);
    }
    return chatClients;
  }

  private static String buildBristlebackMessage() {
    Random random = new Random();
    int randomText = random.nextInt();
    return "{\"name\":\"HelloWorld\",\"payload\":[\"cn\",\"" + randomText + "\"],\"id\":2}";
  }

}
