package pl.bristleback.performance;

public class TestConfiguration {

  private String host;
  private int port;
  private int clientNumber;
  private int messagesNumber;

  public TestConfiguration(String host, int port, int clientNumber, int messagesNumber) {
    this.host = host;
    this.port = port;
    this.clientNumber = clientNumber;
    this.messagesNumber = messagesNumber;
  }

  public String getHost() {
    return host;
  }

  public int getPort() {
    return port;
  }

  public int getClientNumber() {
    return clientNumber;
  }

  public int getMessagesNumber() {
    return messagesNumber;
  }

  @Override
  public String toString() {
    return "url: " + host + ":" + port + ", clients: " + clientNumber + ", total messages: " + messagesNumber;
  }
}