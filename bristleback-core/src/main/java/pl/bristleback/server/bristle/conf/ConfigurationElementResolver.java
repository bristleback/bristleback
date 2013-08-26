package pl.bristleback.server.bristle.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.integration.spring.BristleSpringIntegration;

import java.util.Map;

@Component
public class ConfigurationElementResolver {

  @Autowired
  private BristleSpringIntegration springIntegration;

  public <T> T getConfigurationElement(Class<T> elementClass, T defaultElement) {
    Map<String, T> customFactories = springIntegration.getApplicationBeansOfType(elementClass);
    T chosenElement;
    if (customFactories.isEmpty()) {
      chosenElement = defaultElement;
    } else if (customFactories.size() > 1) {
      throw new BristleInitializationException("Multiple custom configuration elements of type " + elementClass);
    } else {
      chosenElement = customFactories.values().iterator().next();
    }
    return chosenElement;
  }

}
