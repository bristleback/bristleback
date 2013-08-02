package pl.bristleback.server.bristle.serialization.jackson.init;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.conf.ConfigurationElementResolver;

import javax.inject.Inject;

/**
 * Helper class used to retrieve object mapper using {@link ObjectMapperFactory} implementation.
 */
@Component
public class ObjectMapperInitializer {

  @Inject
  private ConfigurationElementResolver elementResolver;

  @Inject
  private SimpleObjectMapperFactory simpleObjectMapperFactory;


  public ObjectMapper initObjectMapper() {
    ObjectMapperFactory objectMapperFactory = elementResolver.getConfigurationElement(ObjectMapperFactory.class, simpleObjectMapperFactory);

    return objectMapperFactory.initObjectMapper();
  }
}
