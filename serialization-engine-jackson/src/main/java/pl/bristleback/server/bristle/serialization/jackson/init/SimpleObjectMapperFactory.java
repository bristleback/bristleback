package pl.bristleback.server.bristle.serialization.jackson.init;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

/**
 * This is a default implementation of {@link ObjectMapperFactory},
 * providing an object mapper instance with all options set with default values.
 */
@Component
public class SimpleObjectMapperFactory implements ObjectMapperFactory {

  @Override
  public ObjectMapper initObjectMapper() {
    return new ObjectMapper();
  }
}
