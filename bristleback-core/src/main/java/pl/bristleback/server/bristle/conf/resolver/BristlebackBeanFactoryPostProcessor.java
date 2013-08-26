package pl.bristleback.server.bristle.conf.resolver;

import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import pl.bristleback.server.bristle.conf.InitialConfiguration;
import pl.bristleback.server.bristle.integration.spring.BristleSpringIntegration;

public class BristlebackBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

  private final InitialConfiguration initialConfiguration;
  private final BristleSpringIntegration springIntegration;

  public BristlebackBeanFactoryPostProcessor(InitialConfiguration initialConfiguration, BristleSpringIntegration springIntegration) {
    this.initialConfiguration = initialConfiguration;
    this.springIntegration = springIntegration;
  }

  @Override
  public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
    beanFactory.registerSingleton("initialConfiguration", initialConfiguration);
    beanFactory.registerSingleton("springIntegration", springIntegration);
  }
}
