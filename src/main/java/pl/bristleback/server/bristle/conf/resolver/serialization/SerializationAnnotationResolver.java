package pl.bristleback.server.bristle.conf.resolver.serialization;

import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.api.SerializationEngine;
import pl.bristleback.server.bristle.api.SerializationResolver;
import pl.bristleback.server.bristle.api.annotations.Serialize;
import pl.bristleback.server.bristle.message.BristleMessage;
import pl.bristleback.server.bristle.serialization.SerializationInput;

import javax.inject.Inject;
import javax.inject.Named;
import java.lang.reflect.Type;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-09-04 16:40:15 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component
public class SerializationAnnotationResolver {

  @Inject
  @Named("serializationEngine")
  private SerializationEngine serializationEngine;

  @Inject
  private SerializationInputResolver inputResolver;

  public Object resolveDefaultSerialization(Type serializedObjectType) {
    return resolveSerialization(serializedObjectType, null);
  }

  public Object resolveSerialization(Type serializedObjectType, Serialize serializeAnnotation) {
    SerializationResolver serializationResolver = serializationEngine.getSerializationResolver();

    SerializationInput input;
    if (serializeAnnotation == null) {
      input = new SerializationInput();
    } else {
      input = inputResolver.resolveInputInformation(serializeAnnotation);
    }

    SerializationInput messageInput = inputResolver.resolveMessageInputInformation(serializedObjectType, input);
    return serializationResolver.resolveSerialization(BristleMessage.class, messageInput);
  }
}