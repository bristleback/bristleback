package pl.bristleback.server.mock.beans;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import pl.bristleback.server.bristle.api.InitialConfigurationResolver;
import pl.bristleback.server.bristle.app.BristlebackServerInstance;
import pl.bristleback.server.bristle.conf.resolver.ServerInstanceResolver;
import pl.bristleback.server.bristle.conf.resolver.spring.SpringApplicationComponentsContainer;

import javax.inject.Inject;

public class SpringMockBeansFactory {

  private static Logger log = Logger.getLogger(SpringMockBeansFactory.class.getName());

  @Inject
  private ApplicationContext applicationContext;

  private ApplicationContext frameworkContext;

  public ApplicationContext getApplicationContext() {
    return applicationContext;
  }

  public BristlebackServerInstance mockServerInstance() {
    InitialConfigurationResolver initialConfigurationResolver = applicationContext.getBean("initPojoConfigResolver", InitialConfigurationResolver.class);
    SpringApplicationComponentsContainer componentsResolver = new SpringApplicationComponentsContainer(applicationContext);
    ServerInstanceResolver instanceResolver = new ServerInstanceResolver(initialConfigurationResolver, componentsResolver);
    BristlebackServerInstance serverInstance = instanceResolver.resolverServerInstance();
    frameworkContext = serverInstance.getConfiguration().getComponentsContainer().getBristlebackFrameworkContext();
    return serverInstance;
  }

  public <T> T getFrameworkBean(String beanName, Class<T> clazz) {
    return frameworkContext.getBean(beanName, clazz);
  }

  public <T> T getFrameworkBean(Class<T> clazz) {
    return frameworkContext.getBean(clazz);
  }
}
