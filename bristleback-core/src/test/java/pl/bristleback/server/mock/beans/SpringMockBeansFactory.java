package pl.bristleback.server.mock.beans;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import pl.bristleback.server.bristle.api.InitialConfigurationResolver;
import pl.bristleback.server.bristle.app.BristlebackServerInstance;
import pl.bristleback.server.bristle.conf.runner.ServerInstanceResolver;

import javax.inject.Inject;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-05-03 17:20:23 <br/>
 *
 * @author Wojciech Niemiec
 */
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
    ServerInstanceResolver instanceResolver = new ServerInstanceResolver(initialConfigurationResolver, applicationContext);
    BristlebackServerInstance serverInstance = instanceResolver.resolverServerInstance();
    frameworkContext = serverInstance.getConfiguration().getSpringIntegration().getBristlebackFrameworkContext();
    return serverInstance;
  }

  public <T> T getFrameworkBean(String beanName, Class<T> clazz) {
    return frameworkContext.getBean(beanName, clazz);
  }

  public <T> T getFrameworkBean(Class<T> clazz) {
    return frameworkContext.getBean(clazz);
  }
}
