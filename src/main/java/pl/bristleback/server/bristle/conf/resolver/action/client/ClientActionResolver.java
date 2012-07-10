package pl.bristleback.server.bristle.conf.resolver.action.client;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.action.ActionParameterInformation;
import pl.bristleback.server.bristle.action.client.ClientActionInformation;
import pl.bristleback.server.bristle.action.client.strategy.ClientActionResponseStrategies;
import pl.bristleback.server.bristle.api.SerializationEngine;
import pl.bristleback.server.bristle.api.SerializationResolver;
import pl.bristleback.server.bristle.api.action.ClientActionSender;
import pl.bristleback.server.bristle.api.annotations.Bind;
import pl.bristleback.server.bristle.api.annotations.ClientAction;
import pl.bristleback.server.bristle.conf.resolver.action.ParameterResolver;
import pl.bristleback.server.bristle.conf.resolver.serialization.SerializationInputResolver;
import pl.bristleback.server.bristle.message.BristleMessage;
import pl.bristleback.server.bristle.serialization.PropertyInformation;
import pl.bristleback.server.bristle.serialization.SerializationInput;

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
  private ParameterResolver parametersResolver;

  @Inject
  private SerializationInputResolver inputResolver;

  @Inject
  private ClientActionResponseStrategies responseStrategies;

  public ClientActionInformation prepareActionInformation(String actionClassName, Method actionMethod) {
    String actionName = resolveActionName(actionMethod);
    String fullName = resolveFullName(actionName, actionClassName);

    List<ActionParameterInformation> parameters = resolveActionParameters(actionMethod);
    Object actionSerialization = resolveActionSerializationInformation(actionMethod, parameters);
    ClientActionSender actionSender = resolveActionResponse(actionMethod);
    return new ClientActionInformation(actionName, fullName, actionSerialization, parameters, actionSender);
  }

  private ClientActionSender resolveActionResponse(Method actionMethod) {
    return responseStrategies.getStrategy(actionMethod.getReturnType());
  }

  private List<ActionParameterInformation> resolveActionParameters(Method actionMethod) {
    List<ActionParameterInformation> parameters = new ArrayList<ActionParameterInformation>();
    for (int i = 0; i < actionMethod.getParameterTypes().length; i++) {
      Type parameterType = actionMethod.getGenericParameterTypes()[i];
      ActionParameterInformation parameterInformation =
        parametersResolver.prepareActionParameter(parameterType, actionMethod.getParameterAnnotations()[i]);
      parameters.add(parameterInformation);
    }
    return parameters;
  }

  private Object resolveActionSerializationInformation(Method actionMethod, List<ActionParameterInformation> parametersInformation) {
    Object[] parameters = actionMethod.getParameterTypes();
    SerializationInput contentSerializationInput;
    Type contentType;
    if (parameters.length == 1) {
      Bind bindAnnotation = findBindAnnotation(actionMethod.getParameterAnnotations()[0]);
      contentType = actionMethod.getGenericParameterTypes()[0];
      contentSerializationInput = resolveSingleParameterInput(bindAnnotation, contentType);
    } else {
      contentSerializationInput = resolveMultipleParametersInput(actionMethod, parametersInformation);
      contentType = HashMap.class;
    }
    SerializationInput messageInput = inputResolver.resolveMessageInputInformation(contentType, contentSerializationInput);
    SerializationResolver serializationResolver = serializationEngine.getSerializationResolver();
    return serializationResolver.resolveSerialization(BristleMessage.class, messageInput);
  }

  private SerializationInput resolveMultipleParametersInput(Method actionMethod, List<ActionParameterInformation> parametersInformation) {
    SerializationInput mapInput = new SerializationInput();

    Map<String, SerializationInput> parametersInput = new HashMap<String, SerializationInput>();
    int index = 0;
    for (int i = 0; i < actionMethod.getGenericParameterTypes().length; i++) {
      if (parametersInformation.get(i).getExtractor().isDeserializationRequired()) {
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

  private SerializationInput resolveSingleParameterInput(Bind bindAnnotation, Type parameterType) {
    if (bindAnnotation == null) {
      SerializationInput input = new SerializationInput();
      PropertyInformation propertyInformation = new PropertyInformation();
      propertyInformation.setType(parameterType);
      input.setPropertyInformation(propertyInformation);
      return input;
    }
    return inputResolver.resolveInputInformation(bindAnnotation);
  }

  private Bind findBindAnnotation(Annotation[] parameterAnnotations) {
    for (Annotation annotation : parameterAnnotations) {
      if (annotation.annotationType().equals(Bind.class)) {
        return (Bind) annotation;
      }
    }
    return null;
  }

  private String resolveFullName(String actionName, String actionClassName) {
    return actionClassName + pl.bristleback.server.bristle.utils.StringUtils.DOT_AS_STRING + actionName;
  }

  private String resolveActionName(Method actionMethod) {
    ClientAction actionAnnotation = AnnotationUtils.findAnnotation(actionMethod, ClientAction.class);
    if (StringUtils.isNotBlank(actionAnnotation.value())) {
      return actionAnnotation.value();
    }
    return actionMethod.getName();
  }
}
