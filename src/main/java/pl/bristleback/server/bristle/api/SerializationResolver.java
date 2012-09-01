package pl.bristleback.server.bristle.api;

import pl.bristleback.server.bristle.serialization.SerializationInput;

import java.lang.reflect.Type;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-01-08 14:07:10 <br/>
 *
 * @author Wojciech Niemiec
 */
public interface SerializationResolver<T> extends ConfigurationAware {

  T resolveDefaultSerialization(Type objectType);

  T resolveSerialization(Type objectType, SerializationInput input);
}
