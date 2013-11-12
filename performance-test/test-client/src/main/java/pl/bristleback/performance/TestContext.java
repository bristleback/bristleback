package pl.bristleback.performance;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class TestContext {

  private TestConfiguration configuration;

  private List<TestWebsocketClient> clients;

  private static final AtomicLong clientsConnected = new AtomicLong(0);
  private static final AtomicLong messagesSent = new AtomicLong(0);
  private static final AtomicLong messagesReceived = new AtomicLong(0);

  public TestContext(TestConfiguration configuration) {
    this.configuration = configuration;
  }

  public boolean allClientsAreConnected() {
    return clients.size() == configuration.getClientNumber();
  }

  public int countConnectedClients() {
    return clientsConnected.intValue();
  }

  public void clientConnected() {
    clientsConnected.incrementAndGet();
  }

  public void clientDisconnected() {
    clientsConnected.decrementAndGet();
  }

  public void messageSent() {
    messagesSent.incrementAndGet();
  }

  public void messageReceived() {
    messagesReceived.incrementAndGet();
  }

  public int countSentMessages() {
    return messagesReceived.intValue();
  }

  public int countReceivedMessages() {
    return messagesReceived.intValue();
  }

  public void setClients(List<TestWebsocketClient> clients) {
    this.clients = clients;
  }

  public TestWebsocketClient getClient(int clientIndex) {
    return clients.get(clientIndex);
  }


  public List<TestWebsocketClient> getAllClients() {
    return clients;
  }
}