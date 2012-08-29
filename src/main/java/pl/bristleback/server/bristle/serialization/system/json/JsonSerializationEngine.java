package pl.bristleback.server.bristle.serialization.system.json;

import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.api.BristlebackConfig;
import pl.bristleback.server.bristle.api.SerializationEngine;
import pl.bristleback.server.bristle.api.SerializationResolver;
import pl.bristleback.server.bristle.serialization.system.PropertySerialization;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-11-11 15:40:11 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component("system.serializer.json")
public class JsonSerializationEngine implements SerializationEngine<PropertySerialization> {

  @Inject
  @Named("system.serializationResolver")
  private SerializationResolver<PropertySerialization> serializationResolver;

  @Inject
  private JsonFastSerializer fastSerializer;

  @Inject
  private JsonFastDeserializer fastDeserializer;

  @Override
  public void init(BristlebackConfig configuration) {
    serializationResolver.init(configuration);
  }

  @Override
  public Object deserialize(String serializedObject, PropertySerialization serialization) throws Exception {
    return fastDeserializer.deserialize(serializedObject, serialization);
  }

  @Override
  public String serialize(Object object, PropertySerialization serialization) throws Exception {
    return fastSerializer.serializeObject(object, serialization);
  }

  @Override
  public String serialize(Object object) throws Exception {
    PropertySerialization serialization = serializationResolver.resolveDefaultSerialization(object.getClass());
    return serialize(object, serialization);
  }

  @Override
  public SerializationResolver<PropertySerialization> getSerializationResolver() {
    return serializationResolver;
  }
}
