package pl.bristleback.server.bristle.conf.runner;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import pl.bristleback.server.bristle.api.BristlebackConfig;
import pl.bristleback.server.bristle.api.InitialConfigurationResolver;
import pl.bristleback.server.bristle.app.BristlebackServerInstance;
import pl.bristleback.server.bristle.conf.InitialConfiguration;
import pl.bristleback.server.bristle.conf.resolver.SpringConfigurationResolver;
import pl.bristleback.server.bristle.integration.spring.BristleSpringIntegration;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-05-01 16:47:44 <br/>
 *
 * @author Wojciech Niemiec
 */
public class ServerInstanceResolver {

  private InitialConfigurationResolver initialConfigurationResolver;
  private ApplicationContext actualApplicationContext;

  public ServerInstanceResolver(InitialConfigurationResolver initialConfigurationResolver, ApplicationContext actualApplicationContext) {
    this.initialConfigurationResolver = initialConfigurationResolver;
    this.actualApplicationContext = actualApplicationContext;
  }

  public BristlebackServerInstance resolverServerInstance() {
    InitialConfiguration initialConfiguration = initialConfigurationResolver.resolveConfiguration();
    startLogger(initialConfiguration);

    AnnotationConfigApplicationContext frameworkContext = new AnnotationConfigApplicationContext();
    frameworkContext.register(SpringConfigurationResolver.class);
    frameworkContext.refresh();
    BristleSpringIntegration springIntegration = new BristleSpringIntegration(actualApplicationContext, frameworkContext);

    SpringConfigurationResolver springConfigurationResolver = frameworkContext.getBean("springConfigurationResolver", SpringConfigurationResolver.class);
    springConfigurationResolver.setSpringIntegration(springIntegration);
    springConfigurationResolver.setInitialConfiguration(initialConfiguration);

    frameworkContext.scan(InitialConfiguration.SYSTEM_BASE_PACKAGES);

    BristlebackConfig configuration = frameworkContext.getBean("bristlebackConfigurationFinal", BristlebackConfig.class);
    return new BristlebackServerInstance(configuration);
  }


  private void startLogger(InitialConfiguration initialConfiguration) {
    BasicConfigurator.configure();
    Logger.getRootLogger().setLevel(initialConfiguration.getLoggingLevel());
    Logger.getLogger("org.apache").setLevel(initialConfiguration.getLoggingLevel());
  }
}
