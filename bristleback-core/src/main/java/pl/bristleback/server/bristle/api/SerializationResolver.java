package pl.bristleback.server.bristle.api;

import pl.bristleback.server.bristle.serialization.SerializationBundle;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-01-08 14:07:10 <br/>
 *
 * @author Wojciech Niemiec
 */
public interface SerializationResolver<T> extends ConfigurationAware {

  SerializationBundle initSerializationBundle(Field objectSenderField);

  T resolveSerialization(Type objectType, Annotation... annotations);

  void setSerializationForField(T parentSerialization, String fieldName, T fieldSerialization);
}
