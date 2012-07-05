package pl.bristleback.server.bristle.conf.resolver.action;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.action.ActionParameterInformation;
import pl.bristleback.server.bristle.action.extractor.ActionExtractorsContainer;
import pl.bristleback.server.bristle.api.BristlebackConfig;
import pl.bristleback.server.bristle.api.SerializationResolver;
import pl.bristleback.server.bristle.api.action.ActionParameterExtractor;
import pl.bristleback.server.bristle.api.annotations.Bind;
import pl.bristleback.server.bristle.conf.resolver.SpringConfigurationResolver;
import pl.bristleback.server.bristle.conf.resolver.serialization.SerializationInputResolver;
import pl.bristleback.server.bristle.exceptions.SerializationResolvingException;
import pl.bristleback.server.bristle.serialization.SerializationInput;
import pl.bristleback.server.bristle.utils.ReflectionUtils;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-07-23 13:13:41 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component
public class ParameterResolver {
  private static Logger log = Logger.getLogger(ParameterResolver.class.getName());

  @Inject
  private SerializationInputResolver serializationInputResolver;

  @Inject
  private ActionExtractorsResolver actionExtractorsResolver;

  @Inject
  @Named(SpringConfigurationResolver.CONFIG_BEAN_NAME)
  private BristlebackConfig configuration;

  private ActionExtractorsContainer actionExtractorsContainer;

  @PostConstruct
  private void init() {
    actionExtractorsContainer = actionExtractorsResolver.resolveParameterExtractors();
  }

  public ActionParameterInformation prepareActionParameter(Type parameterType, Annotation[] parameterAnnotations) {
    ActionParameterInformation parameterInformation = new ActionParameterInformation();

    resolveParamExtractor(parameterInformation, parameterType);
    resolveParameterDetails(parameterInformation, parameterType, parameterAnnotations);
    return parameterInformation;
  }

  private void resolveParameterDetails(ActionParameterInformation parameterInformation, Type parameterType, Annotation[] parameterAnnotations) {
    SerializationResolver serializationResolver = configuration.getSerializationEngine().getSerializationResolver();
    Object propertySerialization = null;
    Bind bindAnnotation = findBindAnnotation(parameterAnnotations);
    if (parameterInformation.getExtractor().isDeserializationRequired()) {
      if (bindAnnotation != null) {
        SerializationInput input = serializationInputResolver.resolveInputInformation(bindAnnotation);
        propertySerialization = serializationResolver.resolveSerialization(parameterType, input);
      } else {
        propertySerialization = serializationResolver.resolveDefaultSerialization(parameterType);
      }
    }
    parameterInformation.setPropertySerialization(propertySerialization);
  }

  private Bind findBindAnnotation(Annotation[] parameterAnnotations) {
    for (Annotation annotation : parameterAnnotations) {
      if (annotation.annotationType().equals(Bind.class)) {
        return (Bind) annotation;
      }
    }
    return null;
  }

  private void resolveParamExtractor(ActionParameterInformation parameterInformation, Type parameterType) {
    Class parameterClass;
    if (parameterType instanceof ParameterizedType) {
      parameterClass = (Class) (((ParameterizedType) parameterType).getRawType());
    } else if (parameterType instanceof Class) {
      parameterClass = (Class) parameterType;
    } else {
      throw new SerializationResolvingException("Incompatible type, cannot resolve parameter extractor: " + parameterType);
    }

    parameterClass = getParameterClass(parameterClass);
    ActionParameterExtractor extractor = actionExtractorsContainer.getParameterExtractor(parameterClass);
    parameterInformation.setExtractor(extractor);
  }

  private Class getParameterClass(Class parameterClass) {
    if (parameterClass.isPrimitive()) {
      return ReflectionUtils.getWrapperClassForPrimitive(parameterClass);
    } else {
      return parameterClass;
    }
  }
}