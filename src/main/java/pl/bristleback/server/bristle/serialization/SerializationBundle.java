package pl.bristleback.server.bristle.serialization;

import java.util.HashMap;
import java.util.Map;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-10-08 14:46:06 <br/>
 *
 * @author Wojciech Niemiec
 */
public class SerializationBundle {

  private Map<Class, Object> serializations = new HashMap<Class, Object>();

  public Object getSerialization(Class payloadType) {
    return serializations.get(payloadType);
  }

  public void addSerialization(Class payloadType, Object serialization) {
    if (isSerializationForPayloadTypeExist(payloadType)) {
      throw new SerializationResolvingException("Default serialization for type " + payloadType + " already exists");
    }
    serializations.put(payloadType, serialization);
  }

  public boolean isSerializationForPayloadTypeExist(Class payloadType) {
    return serializations.containsKey(payloadType);
  }
}