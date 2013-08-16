package pl.bristleback.server.bristle.conf.resolver.spring;

import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import pl.bristleback.server.bristle.conf.BristlebackComponentsContainer;
import pl.bristleback.server.bristle.conf.InitialConfiguration;

public class BristlebackBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

  private final InitialConfiguration initialConfiguration;
  private final BristlebackComponentsContainer componentsContainer;

  public BristlebackBeanFactoryPostProcessor(InitialConfiguration initialConfiguration, BristlebackComponentsContainer componentsContainer) {
    this.initialConfiguration = initialConfiguration;
    this.componentsContainer = componentsContainer;
  }

  @Override
  public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
    beanFactory.registerSingleton("initialConfiguration", initialConfiguration);
    beanFactory.registerSingleton("componentsContainer", componentsContainer);
  }
}
