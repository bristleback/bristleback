package pl.bristleback.performance;

import org.eclipse.jetty.websocket.WebSocketClient;
import org.eclipse.jetty.websocket.WebSocketClientFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BristlebackPerformanceTest {

  private TestConfiguration configuration;
  private TestContext testContext;
  private WebSocketClientFactory clientFactory;

  public BristlebackPerformanceTest(TestConfiguration configuration) {
    this.configuration = configuration;
    this.testContext = new TestContext(configuration);
    clientFactory = new WebSocketClientFactory();
  }

  private void runTests() throws Exception {
    System.out.println("Running tests using configuration: " + configuration);
    WebSocketClient websocketClient = init();
    testOpeningConnections(websocketClient);
    testSendingMessages(websocketClient);
    testClosingConnections(websocketClient);
  }

  private WebSocketClient init() throws Exception {
    WebSocketClient webSocketClient = clientFactory.newWebSocketClient();
    webSocketClient.setMaxIdleTime(300000);
    webSocketClient.setProtocol("system.controller.action");
    clientFactory.start();
    return webSocketClient;
  }

  private void testOpeningConnections(WebSocketClient websocketClient) throws Exception {
    long startTime = System.currentTimeMillis();
    List<TestWebsocketClient> clients = createClients(websocketClient);
    testContext.setClients(clients);
    while (!testContext.allClientsAreConnected()) {
      if (System.currentTimeMillis() > (startTime + websocketClient.getMaxIdleTime())) {
        break;
      }
      Thread.sleep(10);
    }
    long end = System.currentTimeMillis();

    System.err.printf("Opened %d of %d connections to %s:%d in %dms\n", testContext.countConnectedClients(),
      configuration.getClientNumber(), configuration.getHost(), configuration.getPort(), (end - startTime));
  }

  private List<TestWebsocketClient> createClients(WebSocketClient websocketClient) throws Exception {
    List<TestWebsocketClient> clients = new ArrayList<TestWebsocketClient>();
    for (int i = 0; i < configuration.getClientNumber(); i++) {
      clients.add(new TestWebsocketClient(websocketClient, configuration, testContext));
    }
    return clients;
  }

  private void testSendingMessages(WebSocketClient websocketClient) throws IOException, InterruptedException {
    Random random = new Random();
    long start = System.currentTimeMillis();

    int clientIndex = 0;
    for (int i = 0; i < configuration.getMessagesNumber(); i++) {
      if (clientIndex == configuration.getClientNumber() - 1) {
        clientIndex = 0;
      }
      TestWebsocketClient client = testContext.getClient(clientIndex);
      String msg = buildBristlebackMessage(random.nextInt());
      client.send(msg);
      clientIndex++;
    }

    long last = 0;
    long progress = start;
    while (testContext.countReceivedMessages() < configuration.getMessagesNumber()) {
      if (System.currentTimeMillis() > (progress + websocketClient.getMaxIdleTime())) {
        System.out.println("TIMEOUT!");
        break;
      }
      if (testContext.countReceivedMessages() != last) {
        progress = System.currentTimeMillis();
        last = testContext.countReceivedMessages();
      }
      Thread.sleep(1);
    }
    long end = System.currentTimeMillis();
    long messagesSentCount = (testContext.countReceivedMessages() * 1000) / (end - start);
    System.err.printf("Sent/Received %d/%d messages in %dms: %dmsg/s\n",
      testContext.countSentMessages(), testContext.countReceivedMessages(), (end - start), messagesSentCount);

  }

  private String buildBristlebackMessage(int randomText) {
    return "{\"name\":\"HelloWorld\",\"payload\":[\"uc\",\"some-text" + randomText + "\"],\"id\":2}";
  }

  private void testClosingConnections(WebSocketClient websocketClient) throws Exception {
    long start = System.currentTimeMillis();
    for (TestWebsocketClient client : testContext.getAllClients()) {
      client.disconnect();
    }
    while (testContext.countConnectedClients() > 0) {
      if (System.currentTimeMillis() > (start + websocketClient.getMaxIdleTime())) {
        break;
      }
      Thread.sleep(10);
    }
    long end = System.currentTimeMillis();

    System.err.printf("Closed %d connections to %s:%d in %dms\n",
      configuration.getClientNumber(), configuration.getHost(), configuration.getPort(), (end - start));

    clientFactory.stop();
  }

  public static void main(String[] args) throws Exception {
    System.out.println("Bristleback performance test");
    System.out.println("Usage: \n" +
      "mvn -PclientJar clean compile assembly:single \n" +
      "java -jar ./target/performance-test.jar <host|localhost> <port|8765> <clients|10> <total_messages|10000>\n");
    String host = args.length > 0 ? args[0] : "localhost";
    int port = args.length > 1 ? Integer.parseInt(args[1]) : 8765;
    int clientNumber = args.length > 2 ? Integer.parseInt(args[2]) : 10;
    int messagesNumber = args.length > 3 ? Integer.parseInt(args[3]) : 10000;

    TestConfiguration configuration = new TestConfiguration(host, port, clientNumber, messagesNumber);
    BristlebackPerformanceTest test = new BristlebackPerformanceTest(configuration);

    test.runTests();
  }
}
