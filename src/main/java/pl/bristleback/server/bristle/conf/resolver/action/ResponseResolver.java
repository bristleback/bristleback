package pl.bristleback.server.bristle.conf.resolver.action;

import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.action.response.ActionResponseInformation;
import pl.bristleback.server.bristle.api.SerializationEngine;
import pl.bristleback.server.bristle.api.SerializationResolver;

import javax.inject.Inject;
import javax.inject.Named;
import java.lang.reflect.Method;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-06-16 12:13:12 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component
public class ResponseResolver {

  @Inject
  @Named("serializationEngine")
  private SerializationEngine serializationEngine;

  @Inject
  private BristleMessageSerializationUtils messageSerializationUtils;

  ActionResponseInformation resolveResponse(Method action) {
    ActionResponseInformation responseInformation = new ActionResponseInformation();

    Class<?> actionReturnType = action.getReturnType();
    if (actionReturnType.equals(Void.class) || actionReturnType.equals(Void.TYPE)) {
      responseInformation.setVoidResponse(true);
    }

    resolveResponseSerialization(action, responseInformation);

    return responseInformation;
  }

  @SuppressWarnings("unchecked")
  private void resolveResponseSerialization(Method action, ActionResponseInformation responseInformation) {

    SerializationResolver serializationResolver = serializationEngine.getSerializationResolver();
    Object serialization = serializationResolver.resolveSerialization(messageSerializationUtils.getSimpleMessageType());

    Object payloadSerialization = serializationResolver.resolveSerialization(action.getGenericReturnType(), action.getAnnotations());
    serializationResolver.setSerializationForField(serialization, "payload", payloadSerialization);

    responseInformation.setSerialization(serialization);
  }
}
