package pl.bristleback.client.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import pl.bristleback.common.serialization.message.BristleMessage;
import pl.bristleback.server.bristle.serialization.RawMessageSerializationEngine;
import pl.bristleback.server.bristle.serialization.jackson.JacksonSerialization;
import pl.bristleback.server.bristle.serialization.jackson.JacksonSerializationEngine;
import pl.bristleback.server.bristle.serialization.jackson.JacksonSerializationResolver;

/**
 * <p/>
 * Created on: 31.07.13 20:12 <br/>
 *
 * @author Pawel Machowski
 */
public class FromJsonDeserializer {

  public Object jsonToObject(String json, Class<?> objectType) {
    JacksonSerializationEngine engine = JacksonSerializationEngine.simpleEngine();
    JacksonSerializationResolver serializationResolver = new JacksonSerializationResolver();
    serializationResolver.setObjectMapper(new ObjectMapper());
    JacksonSerialization jacksonSerialization = serializationResolver.resolveSerialization(objectType);
    try {
      return engine.deserialize(json, jacksonSerialization);
    } catch (Exception e) {
      //TODO handle
      e.printStackTrace();
    }
    return null;
  }

  public BristleMessage<String[]> jsonToBristleMessage(String json) {
    RawMessageSerializationEngine rawMessageSerializationEngine = new RawMessageSerializationEngine();
    BristleMessage<String[]> bristleMessage = rawMessageSerializationEngine.deserialize(json);
    return bristleMessage;
  }
}
