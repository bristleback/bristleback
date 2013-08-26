package pl.bristleback.client.actions.arguments;

import pl.bristleback.client.serialization.FromJsonDeserializer;

/**
 * <p/>
 * Created on: 11.08.13 19:54 <br/>
 *
 * @author Pawel Machowski
 */
public class Arguments {

  private FromJsonDeserializer deserializer = new FromJsonDeserializer();
  private String[] payload;

  public Arguments(String[] payload) {
    this.payload = payload;
  }

  public <T> T get(int index, Class<T> type) {
    String payloadArgument = payload[index];
    return (T) deserializer.jsonToObject(payloadArgument, type);
  }

}
