package pl.bristleback.server.bristle.serialization.jackson.init;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Classes implementing this interface are responsible for creating {@link ObjectMapper} objects,
 * which are then used by {@link pl.bristleback.server.bristle.serialization.jackson.JacksonSerializationEngine}
 * in serialization and deserialization operations. To provide custom object mapper instance,
 * simply create Spring bean that implements this interface.
 *
 * @see SimpleObjectMapperFactory default object mapper factory
 */
public interface ObjectMapperFactory {

  /**
   * Creates object mapper instance and initializes it using serialization/deserialization options.
   * For more details about how to customize Jackson, please read Jackson documentation.
   *
   * @return fully initialized Jackson object mapper.
   */
  ObjectMapper initObjectMapper();
}
