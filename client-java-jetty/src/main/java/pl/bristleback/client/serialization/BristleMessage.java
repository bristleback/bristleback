package pl.bristleback.client.serialization;

/**
 * <p/>
 * Created on: 5/31/13 2:02 PM <br/>
 *
 * @author Pawel Machowski
 */
//TODO use common class from sever if extracted to common module
public class BristleMessage {
  public static final String PAYLOAD_PROPERTY_NAME = "payload";

  private String id;
  private String name;
  private Object[] payload;

  public BristleMessage withId(String messageId) {
    this.id = messageId;
    return this;
  }

  public BristleMessage withName(String messageName) {
    this.name = messageName;
    return this;
  }

  public BristleMessage withPayload(Object payloadContent) {
    this.payload = new Object[]{payloadContent};
    return this;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Object[] getPayload() {
    return payload;
  }

  public void setPayload(Object[] payload) {
    this.payload = payload;
  }
}
