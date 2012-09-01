package pl.bristleback.server.bristle.conf.resolver.action;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.action.extractor.ActionExtractorsContainer;
import pl.bristleback.server.bristle.api.BristlebackConfig;
import pl.bristleback.server.bristle.api.action.ActionParameterExtractor;
import pl.bristleback.server.bristle.conf.resolver.SpringConfigurationResolver;
import pl.bristleback.server.bristle.integration.spring.BristleSpringIntegration;
import pl.bristleback.server.bristle.utils.ReflectionUtils;

import javax.inject.Inject;
import javax.inject.Named;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-01-08 14:44:34 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component
public class ActionExtractorsResolver {
  private static Logger log = Logger.getLogger(ActionExtractorsResolver.class.getName());

  @Inject
  private BristleSpringIntegration springIntegration;

  @Inject
  @Named(SpringConfigurationResolver.CONFIG_BEAN_NAME)
  private BristlebackConfig configuration;

  public ActionExtractorsContainer resolveParameterExtractors() {
    Map<Class, ActionParameterExtractor> extractors = new HashMap<Class, ActionParameterExtractor>();
    resolveDefaultParameterExtractors(extractors);
    resolveCustomParameterExtractors(extractors);

    ActionExtractorsContainer actionExtractorsContainer = new ActionExtractorsContainer();
    actionExtractorsContainer.setParameterExtractors(extractors);
    initParameterExtractors(actionExtractorsContainer);
    return actionExtractorsContainer;
  }

  private void initParameterExtractors(ActionExtractorsContainer actionExtractorsContainer) {
    for (ActionParameterExtractor extractor : actionExtractorsContainer.getParameterExtractors().values()) {
      extractor.init(configuration);
    }
  }

  private void resolveCustomParameterExtractors(Map<Class, ActionParameterExtractor> extractors) {
    Map<String, ActionParameterExtractor> extractorInstances = springIntegration.getApplicationBeansOfType(ActionParameterExtractor.class);
    addExtractors(extractors, extractorInstances, ActionParameterExtractor.class);
  }

  private void resolveDefaultParameterExtractors(Map<Class, ActionParameterExtractor> extractors) {
    Map<String, ActionParameterExtractor> extractorInstances = springIntegration.getFrameworkBeansOfType(ActionParameterExtractor.class);
    addExtractors(extractors, extractorInstances, ActionParameterExtractor.class);
  }

  private <T> void addExtractors(Map<Class, T> extractors, Map<String, T> extractorsContainer, Class<T> parametrizedInterface) {
    for (T extractor : extractorsContainer.values()) {
      Class extractorClass = extractor.getClass();
      Class parameterClass = getParameterClass(extractorClass, parametrizedInterface);
      extractors.put(parameterClass, extractor);
      Class primitiveForParameterClass = ReflectionUtils.getPrimitiveForWrapper(parameterClass);
      if (primitiveForParameterClass != null) {
        if (!extractors.containsKey(primitiveForParameterClass)) {
          extractors.put(primitiveForParameterClass, extractor);
        }
      }
    }
  }

  private Class getParameterClass(Class extractorClass, Class parametrizedInterface) {
    for (int i = 0; i < extractorClass.getInterfaces().length; i++) {
      Class interfaceOfClass = extractorClass.getInterfaces()[i];
      if (parametrizedInterface.equals(interfaceOfClass)) {
        Type genericInterface = extractorClass.getGenericInterfaces()[i];
        if (genericInterface instanceof ParameterizedType) {
          return (Class) ((ParameterizedType) (genericInterface)).getActualTypeArguments()[0];
        }
        return Object.class;
      }
    }
    throw new RuntimeException("Cannot find " + parametrizedInterface.getSimpleName() + " interface, this exception should never happen.");
  }
}
