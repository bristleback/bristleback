package pl.bristleback.server.bristle.conf.resolver.action.client;

import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.action.client.ClientActionInformation;
import pl.bristleback.server.bristle.action.client.ClientActionParameterInformation;
import pl.bristleback.server.bristle.action.client.ClientActionUtils;
import pl.bristleback.server.bristle.action.client.strategy.ClientActionResponseStrategies;
import pl.bristleback.server.bristle.api.SerializationEngine;
import pl.bristleback.server.bristle.api.SerializationResolver;
import pl.bristleback.server.bristle.api.action.ClientActionSender;
import pl.bristleback.server.bristle.api.annotations.Bind;
import pl.bristleback.server.bristle.conf.resolver.serialization.SerializationInputResolver;
import pl.bristleback.server.bristle.exceptions.SerializationResolvingException;
import pl.bristleback.server.bristle.message.BristleMessage;
import pl.bristleback.server.bristle.serialization.PropertyInformation;
import pl.bristleback.server.bristle.serialization.SerializationInput;
import pl.bristleback.server.bristle.utils.ReflectionUtils;

import javax.inject.Inject;
import javax.inject.Named;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-07-05 21:13:21 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component
public class ClientActionResolver {

  @Inject
  @Named("serializationEngine")
  private SerializationEngine serializationEngine;

  @Inject
  private ClientActionParameterResolver parameterResolver;

  @Inject
  private SerializationInputResolver inputResolver;

  @Inject
  private ClientActionResponseStrategies responseStrategies;

  public ClientActionInformation prepareActionInformation(String actionClassName, Method actionMethod) {
    String actionName = ClientActionUtils.resolveActionName(actionMethod);
    String fullName = ClientActionUtils.resolveFullName(actionName, actionClassName);

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

  private Object resolveActionSerializationInformation(Method actionMethod, List<ClientActionParameterInformation> parametersInformation) {
    Object[] parameters = actionMethod.getParameterTypes();
    SerializationInput contentSerializationInput;
    Type contentType;
    if (parameters.length == 1) {
      Bind bindAnnotation = findBindAnnotation(actionMethod.getParameterAnnotations()[0]);
      contentType = actionMethod.getGenericParameterTypes()[0];
      contentSerializationInput = resolveSingleParameterInput(bindAnnotation, contentType);
    } else {
      int parametersToSerializeCount = getNumberOfParametersToSerialize(parametersInformation);
      if (parametersToSerializeCount == 0) {
        return null;
      } else if (parametersToSerializeCount == 1) {
        contentSerializationInput = resolveSingleInputFromMultipleParameters(actionMethod, parametersInformation);
        contentType = contentSerializationInput.getPropertyInformation().getType();
      } else {
        contentSerializationInput = resolveMapInput(actionMethod, parametersInformation);
        contentType = HashMap.class;
      }

    }
    SerializationInput messageInput = inputResolver.resolveMessageInputInformation(contentType, contentSerializationInput);
    SerializationResolver serializationResolver = serializationEngine.getSerializationResolver();
    return serializationResolver.resolveSerialization(BristleMessage.class, messageInput);
  }

  private SerializationInput resolveSingleInputFromMultipleParameters(Method actionMethod, List<ClientActionParameterInformation> parametersInformation) {
    for (int i = 0; i < actionMethod.getGenericParameterTypes().length; i++) {
      if (parametersInformation.get(i).isForSerialization()) {
        Bind bindAnnotation = findBindAnnotation(actionMethod.getParameterAnnotations()[i]);
        return resolveSingleParameterInput(bindAnnotation, actionMethod.getGenericParameterTypes()[i]);
      }
    }
    throw new SerializationResolvingException("Should never happen");
  }

  private SerializationInput resolveMapInput(Method actionMethod, List<ClientActionParameterInformation> parametersInformation) {
    SerializationInput mapInput = new SerializationInput();
    Map<String, SerializationInput> parametersInput = new HashMap<String, SerializationInput>();
    int index = 0;
    for (int i = 0; i < actionMethod.getGenericParameterTypes().length; i++) {
      if (parametersInformation.get(i).isForSerialization()) {
        String parameterName = "p" + index;
        Bind bindAnnotation = findBindAnnotation(actionMethod.getParameterAnnotations()[i]);
        SerializationInput parameterInput = resolveSingleParameterInput(bindAnnotation, actionMethod.getGenericParameterTypes()[i]);
        parametersInput.put(parameterName, parameterInput);
        index++;
      }
    }

    mapInput.setNonDefaultProperties(parametersInput);
    return mapInput;
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

  private SerializationInput resolveSingleParameterInput(Bind bindAnnotation, Type parameterType) {
    if (bindAnnotation == null) {
      SerializationInput input = new SerializationInput();
      PropertyInformation propertyInformation = new PropertyInformation();
      propertyInformation.setType(parameterType);
      input.setPropertyInformation(propertyInformation);
      return input;
    }
    SerializationInput input = inputResolver.resolveInputInformation(bindAnnotation);
    input.getPropertyInformation().setType(parameterType);
    return input;
  }

  private Bind findBindAnnotation(Annotation[] parameterAnnotations) {
    return ReflectionUtils.findAnnotation(parameterAnnotations, Bind.class);
  }
}
