package pl.bristleback.server.bristle.conf.resolver.action.client;

import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.action.client.ClientActionInformation;
import pl.bristleback.server.bristle.action.client.ClientActionParameterInformation;
import pl.bristleback.server.bristle.action.client.strategy.ClientActionResponseStrategies;
import pl.bristleback.server.bristle.api.SerializationEngine;
import pl.bristleback.server.bristle.api.SerializationResolver;
import pl.bristleback.server.bristle.api.action.ClientActionSender;
import pl.bristleback.server.bristle.conf.resolver.action.ActionResolvingUtils;
import pl.bristleback.server.bristle.conf.resolver.action.BristleMessageSerializationUtils;
import pl.bristleback.server.bristle.serialization.SerializationResolvingException;

import javax.inject.Inject;
import javax.inject.Named;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-07-05 21:13:21 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component
public class ClientActionResolver {

  private static final boolean ACTION_NAME_SHOULD_BE_VALIDATED = true;

  @Inject
  @Named("serializationEngine")
  private SerializationEngine serializationEngine;

  @Inject
  private BristleMessageSerializationUtils messageSerializationUtils;

  @Inject
  private ClientActionParameterResolver parameterResolver;

  @Inject
  private ClientActionResponseStrategies responseStrategies;

  public ClientActionInformation prepareActionInformation(String actionClassName, Method actionMethod) {
    String actionName = ActionResolvingUtils.resolveClientActionName(actionMethod, ACTION_NAME_SHOULD_BE_VALIDATED);
    String fullName = ActionResolvingUtils.resolveFullName(actionName, actionClassName);

    List<ClientActionParameterInformation> parameters = resolveActionParameters(actionMethod);
    Object actionSerialization = resolveActionSerializationInformation(actionMethod, parameters);
    ClientActionSender actionSender = resolveActionResponse(actionMethod);
    return new ClientActionInformation(actionName, fullName, actionSerialization, parameters, actionSender);
  }

  private ClientActionSender resolveActionResponse(Method actionMethod) {
    return responseStrategies.getStrategy(actionMethod.getReturnType());
  }

  private List<ClientActionParameterInformation> resolveActionParameters(Method actionMethod) {
    List<ClientActionParameterInformation> parameters = new ArrayList<ClientActionParameterInformation>();
    for (int i = 0; i < actionMethod.getParameterTypes().length; i++) {
      Type parameterType = actionMethod.getGenericParameterTypes()[i];

      ClientActionParameterInformation parameterInformation =
        parameterResolver.prepareActionParameter(parameterType, actionMethod.getParameterAnnotations()[i]);
      parameters.add(parameterInformation);
    }
    return parameters;
  }

  @SuppressWarnings("unchecked")
  private Object resolveActionSerializationInformation(Method actionMethod, List<ClientActionParameterInformation> parametersInformation) {
    SerializationResolver serializationResolver = serializationEngine.getSerializationResolver();

    Object payloadSerialization;
    int parametersToSerializeCount = getNumberOfParametersToSerialize(parametersInformation);
    if (parametersToSerializeCount == 0) {
      payloadSerialization = serializationResolver.resolveSerialization(String.class);
    } else if (parametersToSerializeCount == 1) {
      payloadSerialization = resolveSingleSerializationFromMultipleParameters(actionMethod, parametersInformation);
    } else {
      payloadSerialization = resolveParametersMapSerialization(actionMethod, parametersInformation);
    }

    Object messageSerialization = serializationResolver.resolveSerialization(messageSerializationUtils.getSimpleMessageType());
    serializationResolver.setSerializationForField(messageSerialization, "payload", payloadSerialization);

    return messageSerialization;
  }

  private Object resolveSingleSerializationFromMultipleParameters(Method actionMethod, List<ClientActionParameterInformation> parametersInformation) {
    for (int i = 0; i < actionMethod.getGenericParameterTypes().length; i++) {
      if (parametersInformation.get(i).isForSerialization()) {
        return serializationEngine.getSerializationResolver().resolveSerialization(actionMethod.getGenericParameterTypes()[i], actionMethod.getParameterAnnotations()[i]);
      }
    }
    throw new SerializationResolvingException("Should never happen");
  }

  @SuppressWarnings("unchecked")
  private Object resolveParametersMapSerialization(Method actionMethod, List<ClientActionParameterInformation> parametersInformation) {
    SerializationResolver serializationResolver = serializationEngine.getSerializationResolver();
    int index = 0;
    Object payloadSerialization = serializationResolver
      .resolveSerialization(messageSerializationUtils.getSimpleMapType(), messageSerializationUtils.getSimpleMapAnnotations());
    for (int i = 0; i < actionMethod.getGenericParameterTypes().length; i++) {
      if (parametersInformation.get(i).isForSerialization()) {
        String parameterName = "p" + index;

        Object parameterSerialization = serializationResolver
          .resolveSerialization(actionMethod.getGenericParameterTypes()[i], actionMethod.getParameterAnnotations()[i]);

        serializationResolver.setSerializationForField(payloadSerialization, parameterName, parameterSerialization);
        index++;
      }
    }

    return payloadSerialization;
  }

  private int getNumberOfParametersToSerialize(List<ClientActionParameterInformation> parametersInformation) {
    int parametersToSerializeCount = 0;
    for (ClientActionParameterInformation parameterInformation : parametersInformation) {
      if (parameterInformation.isForSerialization()) {
        parametersToSerializeCount++;
      }
    }
    return parametersToSerializeCount;
  }
}
