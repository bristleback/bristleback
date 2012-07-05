package pl.bristleback.server.bristle.message.sender;

/**
 * Pawel Machowski
 * created at 18.05.12 20:21
 */
public class BristleMessage<T> {

  public static final String PAYLOAD_PROPERTY_NAME = "payload";

  private String id;
  private String name;
  private T payload;

  public BristleMessage<T> withId(String messageId) {
    this.id = messageId;
    return this;
  }

  public BristleMessage<T> withName(String messageName) {
    this.name = messageName;
    return this;
  }

  public BristleMessage<T> withPayload(T payloadContent) {
    this.payload = payloadContent;
    return this;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public T getPayload() {
    return payload;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setPayload(T payload) {
    this.payload = payload;
  }
}
