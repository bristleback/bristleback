package pl.bristleback.server.bristle.serialization.jackson;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.api.BristlebackConfig;
import pl.bristleback.server.bristle.api.SerializationResolver;
import pl.bristleback.server.bristle.exceptions.SerializationResolvingException;
import pl.bristleback.server.bristle.serialization.SerializationBundle;
import pl.bristleback.server.bristle.serialization.SerializationInput;
import pl.bristleback.server.bristle.serialization.system.annotation.Serialize;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-03-09 18:45:49 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component("jacksonSerializer.serializationResolver")
public class JacksonSerializationResolver implements SerializationResolver<JacksonSerialization> {

  private ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public void init(BristlebackConfig configuration) {

  }

  @Override
  public SerializationBundle initSerializationBundle(Field objectSenderField) {
    return new SerializationBundle();
  }

  @Override
  public JacksonSerialization resolveSerialization(Type objectType, Annotation... annotations) {
    return null;
  }

  //  @Override
  public JacksonSerialization resolveDefaultSerialization(Type objectType) {
    return resolveSerialization(objectType, new SerializationInput());
  }

  //  @Override
  public JacksonSerialization resolveSerialization(Type objectType, SerializationInput input) {
    JacksonSerialization serialization = new JacksonSerialization();
    if (objectType instanceof ParameterizedType) {
      JavaType rootType = objectMapper.constructType(objectType);
      serialization.setType(rootType);
      return serialization;
    }
    Class objectClass = (Class) objectType;
    JavaType rootType;
    if (Collection.class.isAssignableFrom(objectClass)) {
      rootType = createCollectionSerialization(objectClass, input);
    } else if (Map.class.isAssignableFrom(objectClass)) {
      rootType = createMapSerialization(objectClass, input);
    } else {
      rootType = objectMapper.constructType(objectType);
    }

    serialization.setType(rootType);
    return serialization;
  }

  @SuppressWarnings("unchecked")
  private JavaType createMapSerialization(Class objectClass, SerializationInput input) {
    assertInputHasContainerElementType(input);
    Class containerElementType = input.getPropertyInformation().getElementClass();
    return objectMapper.getTypeFactory().constructMapType(objectClass, String.class, containerElementType);
  }

  @SuppressWarnings("unchecked")
  public JavaType createCollectionSerialization(Class objectClass, SerializationInput input) {
    assertInputHasContainerElementType(input);
    Class containerElementType = input.getPropertyInformation().getElementClass();
    return objectMapper.getTypeFactory().constructCollectionType(objectClass, containerElementType);
  }

  public void assertInputHasContainerElementType(SerializationInput input) {
    if (input.getPropertyInformation().getElementClass().equals(Object.class)) {
      throw new SerializationResolvingException("Error while resolving container element type, such information is not available. \n "
        + "Provide parametrised type of container or specify container element type via " + Serialize.class.getSimpleName() + " annotation");
    }
  }

  @Override
  public void setSerializationForField(JacksonSerialization parentSerialization, String fieldName, JacksonSerialization fieldSerialization) {

  }
}
