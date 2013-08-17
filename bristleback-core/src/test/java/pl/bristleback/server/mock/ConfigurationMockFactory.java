package pl.bristleback.server.mock;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import pl.bristleback.server.bristle.conf.BristleConfig;
import pl.bristleback.server.bristle.conf.BristlebackComponentsContainer;
import pl.bristleback.server.bristle.conf.EngineConfig;
import pl.bristleback.server.bristle.conf.InitialConfiguration;
import pl.bristleback.server.bristle.conf.resolver.spring.SpringApplicationComponentsContainer;

import java.util.Arrays;
import java.util.HashSet;

import static org.mockito.Mockito.mock;

public class ConfigurationMockFactory {

  private static Logger log = Logger.getLogger(ConfigurationMockFactory.class.getName());

  private static final String DEFAULT_MOCK_PROTOCOL_NAME = "bristle-action";

  private static final String DEFAULT_SERIALIZATION_ENGINE = "json";

  private static final String DEFAULT_MOCK_BASE_PACKAGE = "sample";

  private static final String DEFAULT_MOCK_ENGINE_NAME = "netty";

  private static final int DEFAULT_MOCK_PORT = 7890;

  public static BristleConfig getConfiguration(ApplicationContext applicationContext) {
    BristleConfig configuration = new BristleConfig();
    InitialConfiguration initialConfiguration = getInitialConfiguration(applicationContext);
    configuration.setInitialConfiguration(initialConfiguration);
    configuration.setSpringIntegration(initialConfiguration.getSpringIntegration());
    return configuration;
  }

  public static InitialConfiguration getInitialConfiguration(ApplicationContext applicationContext) {
    InitialConfiguration initialConfiguration = new InitialConfiguration();
    initialConfiguration.setAcceptedControllerNames(new HashSet<String>(Arrays.asList(DEFAULT_MOCK_PROTOCOL_NAME)));
    initialConfiguration.setDefaultControllerName(DEFAULT_MOCK_PROTOCOL_NAME);
    initialConfiguration.setSerializationEngine(DEFAULT_SERIALIZATION_ENGINE);
    initialConfiguration.setSpringIntegration(getSpringIntegration(applicationContext));
    getEngineConfiguration(initialConfiguration);

    return initialConfiguration;
  }

  private static BristlebackComponentsContainer getSpringIntegration(ApplicationContext applicationContext) {
    ApplicationContext frameworkContext = mock(ApplicationContext.class);

    SpringApplicationComponentsContainer componentsResolver = new SpringApplicationComponentsContainer(applicationContext);
    return new BristlebackComponentsContainer(componentsResolver, frameworkContext);
  }

  private static void getEngineConfiguration(InitialConfiguration initialConfiguration) {
    EngineConfig engineConfiguration = new EngineConfig();
    engineConfiguration.setPort(DEFAULT_MOCK_PORT);
    engineConfiguration.setName(DEFAULT_MOCK_ENGINE_NAME);
    initialConfiguration.setEngineConfiguration(engineConfiguration);
  }
}