package pl.bristleback.server.bristle.api;

import pl.bristleback.server.bristle.serialization.SerializationBundle;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;

/**
 * Serialization information resolvers cooperate with {@link SerializationEngine} implementations
 * by providing meta information about serialized/deserialized type for them.
 * They also should be able to actualize serialization information object with information about child field.
 * Type of serialization information object is determined by the serialization engine cooperating with this
 * serialization resolver implementation.
 * Serialization resolvers must provide functionality of processing
 * {@link pl.bristleback.server.bristle.message.ConditionObjectSender} fields and initializing
 * {@link SerializationBundle} object.
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
