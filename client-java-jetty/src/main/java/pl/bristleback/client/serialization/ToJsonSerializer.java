package pl.bristleback.client.serialization;

import pl.bristleback.server.bristle.serialization.jackson.JacksonSerializationEngine;

/**
 * <p/>
 * Created on: 5/31/13 1:11 PM <br/>
 *
 * @author Pawel Machowski
 */
public class ToJsonSerializer {


  public String objectToJson(Object object) {
    JacksonSerializationEngine serializationEngine = new JacksonSerializationEngine();
    serializationEngine.init(null);
    try {
      return serializationEngine.serialize(object);
    } catch (Exception e) {
      //TODO handle
      System.out.println(e);
    }
    return "";
  }
}
