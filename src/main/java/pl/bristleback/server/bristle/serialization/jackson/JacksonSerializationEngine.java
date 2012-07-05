package pl.bristleback.server.bristle.serialization.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.api.BristlebackConfig;
import pl.bristleback.server.bristle.api.SerializationEngine;
import pl.bristleback.server.bristle.api.SerializationResolver;
import pl.bristleback.server.bristle.serialization.FormatType;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-03-09 18:27:48 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component("system.jacksonSerializer")
public class JacksonSerializationEngine implements SerializationEngine<JacksonSerialization> {
  private static Logger log = Logger.getLogger(JacksonSerializationEngine.class.getName());

  @Inject
  @Named("jacksonSerializer.serializationResolver")
  private SerializationResolver<JacksonSerialization> serializationResolver;

  private ObjectMapper mapper;

  @Override
  public void init(BristlebackConfig configuration) {
    mapper = new ObjectMapper();
  }

  @Override
  public SerializationResolver<JacksonSerialization> getSerializationResolver() {
    return serializationResolver;
  }

  @Override
  public Object deserialize(String serializedObject, JacksonSerialization serialization) throws Exception {
    return mapper.readValue(serializedObject, serialization.getType());
  }

  @Override
  public String serialize(Object object, JacksonSerialization serialization) throws Exception {
    return mapper.writeValueAsString(object);
  }

  @Override
  public String serialize(Object object) throws Exception {
    return mapper.writeValueAsString(object);
  }

  @Override
  public FormatType getFormatType() {
    return FormatType.JSON;
  }

  public void setSerializationResolver(SerializationResolver<JacksonSerialization> serializationResolver) {
    this.serializationResolver = serializationResolver;
  }

  public void setMapper(ObjectMapper mapper) {
    this.mapper = mapper;
  }
}
