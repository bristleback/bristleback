package pl.bristleback.server.bristle.conf.resolver;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import pl.bristleback.server.bristle.api.ConnectionStateListener;
import pl.bristleback.server.bristle.api.DataController;
import pl.bristleback.server.bristle.api.MessageDispatcher;
import pl.bristleback.server.bristle.api.SerializationEngine;
import pl.bristleback.server.bristle.api.ServerEngine;
import pl.bristleback.server.bristle.api.users.UserContext;
import pl.bristleback.server.bristle.api.users.UserContextFactory;
import pl.bristleback.server.bristle.conf.BristleConfig;
import pl.bristleback.server.bristle.conf.DataControllers;
import pl.bristleback.server.bristle.conf.EngineConfig;
import pl.bristleback.server.bristle.conf.InitialConfiguration;
import pl.bristleback.server.bristle.conf.MessageConfiguration;
import pl.bristleback.server.bristle.conf.UserConfiguration;
import pl.bristleback.server.bristle.conf.resolver.message.ObjectSenderInitializer;
import pl.bristleback.server.bristle.conf.resolver.message.ObjectSenderInjector;
import pl.bristleback.server.bristle.engine.user.DefaultUserContextFactory;
import pl.bristleback.server.bristle.exceptions.BristleInitializationException;
import pl.bristleback.server.bristle.integration.spring.BristleSpringIntegration;
import pl.bristleback.server.bristle.listener.ListenersContainer;
import pl.bristleback.server.bristle.message.ConditionObjectSender;
import pl.bristleback.server.bristle.security.UsersContainer;
import pl.bristleback.server.bristle.utils.ReflectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-04-30 15:54:19 <br/>
 *
 * @author Wojciech Niemiec
 */
@Configuration
@Lazy
public class SpringConfigurationResolver {

  public static final String CONFIG_BEAN_NAME = "bristlebackConfiguration";

  private BristleSpringIntegration springIntegration;

  private InitialConfiguration initialConfiguration;

  @Bean
  public BristleConfig bristlebackConfigurationFinal() {
    BristleConfig configuration = bristlebackConfiguration();

    configuration.setServerEngine(serverEngine());
    configuration.setSerializationEngine(serializationEngine());
    configuration.setMessageConfiguration(messageConfiguration());
    configuration.setDataControllers(dataControllers());
    configuration.setListenersContainer(listenersContainer());
    configuration.setUserConfiguration(userConfiguration());
    initObjectSenders();

    return configuration;
  }

  @Bean(name = CONFIG_BEAN_NAME)
  public BristleConfig bristlebackConfiguration() {
    BristleConfig configuration = new BristleConfig();

    configuration.setSpringIntegration(springIntegration);

    configuration.setInitialConfiguration(initialConfiguration);
    return configuration;
  }

  @Bean
  public BristleSpringIntegration springIntegration() {
    return springIntegration;
  }

  @Bean
  public ServerEngine serverEngine() {
    EngineConfig engineConfiguration = initialConfiguration.getEngineConfiguration();
    String expectedEngineName = engineConfiguration.getName();

    ServerEngine serverEngine = springIntegration.getBean(expectedEngineName, ServerEngine.class);
    serverEngine.init(bristlebackConfiguration());

    return serverEngine;
  }

  @Bean
  public SerializationEngine serializationEngine() {
    String serializationEngineName = initialConfiguration.getSerializationEngine();

    SerializationEngine serializationEngine = springIntegration.getBean(serializationEngineName, SerializationEngine.class);
    serializationEngine.init(bristlebackConfiguration());

    return serializationEngine;
  }

  @Bean
  public DataControllers dataControllers() {
    Map<String, DataController> dataControllersMap = new HashMap<String, DataController>();
    for (String acceptedControllerName : initialConfiguration.getAcceptedControllerNames()) {
      DataController controller = springIntegration.getBean(acceptedControllerName, DataController.class);
      controller.init(bristlebackConfiguration());

      dataControllersMap.put(acceptedControllerName, controller);
    }
    DataController defaultController = dataControllersMap.get(initialConfiguration.getDefaultControllerName());

    return new DataControllers(dataControllersMap, defaultController);
  }

  @Bean
  public ListenersContainer listenersContainer() {
    // framework handlers will always run first
    Map<String, ConnectionStateListener> frameworkHandlers = springIntegration.getFrameworkBeansOfType(ConnectionStateListener.class);
    Map<String, ConnectionStateListener> customHandlers = springIntegration.getApplicationBeansOfType(ConnectionStateListener.class);
    List<ConnectionStateListener> connectionStateListeners = new ArrayList<ConnectionStateListener>(frameworkHandlers.values());
    connectionStateListeners.addAll(customHandlers.values());
    return new ListenersContainer(connectionStateListeners);
  }

  @Bean
  public MessageConfiguration messageConfiguration() {
    MessageConfiguration messageConfiguration = new MessageConfiguration();
    messageConfiguration.setMessageDispatcher(messageDispatcher());
    return messageConfiguration;
  }

  @Bean
  public MessageDispatcher messageDispatcher() {
    String dispatcherName = initialConfiguration.getMessageDispatcher();
    MessageDispatcher dispatcher = springIntegration.getBean(dispatcherName, MessageDispatcher.class);
    dispatcher.setServer(serverEngine());

    return dispatcher;
  }

  public void initObjectSenders() {
    ObjectSenderInitializer objectSenderInitializer = objectSenderInitializer();
    List<ConditionObjectSender> senders = springIntegration.getApplicationBean(ObjectSenderInjector.class).getRegisteredSenders();
    for (ConditionObjectSender sender : senders) {
      objectSenderInitializer.initObjectSender(sender);
    }
  }

  private ObjectSenderInitializer objectSenderInitializer() {
    return springIntegration.getFrameworkBean("objectSenderInitializer", ObjectSenderInitializer.class);
  }

  @SuppressWarnings("unchecked")
  @Bean
  public UserConfiguration userConfiguration() {

    UserContextFactory userContextFactory = userFactory();
    Class<? extends UserContext> userContextClass =
      (Class<? extends UserContext>) ReflectionUtils.getParameterTypes(userFactory().getClass(), UserContextFactory.class)[0];

    UsersContainer usersContainer = springIntegration().getBean(UsersContainer.class);

    return new UserConfiguration(userContextFactory, userContextClass, usersContainer);
  }

  @Bean
  public UserContextFactory userFactory() {
    String userFactorySpecifiedByInApplication = initialConfiguration.getUserFactory();
    if (StringUtils.isBlank(userFactorySpecifiedByInApplication)) {
      Map<String, UserContextFactory> userFactoryBeans = springIntegration.getApplicationBeansOfType(UserContextFactory.class);
      if (userFactoryBeans.size() == 0) { //no beans found
        return new DefaultUserContextFactory();
      }
      if (userFactoryBeans.size() == 1) { //one bean found in application configuration
        return springIntegration.getApplicationBean(UserContextFactory.class);
      }
      if (userFactoryBeans.size() > 1) { //more than one bean found in application configuration (initial configuration doesn't contain property which one to choose
        throw new BristleInitializationException("Found more than one implementation of class"
          + UserContextFactory.class.getName() + ". "
          + "Please specify in in initial configuration which one should be used");
      }
    } else {
      return springIntegration.getApplicationBean(userFactorySpecifiedByInApplication, UserContextFactory.class);
    }
    throw new BristleInitializationException("Could not find implementation of "
      + UserContextFactory.class.getName() + "."
      + "Please specify one in initial configuration");
  }

  public void setSpringIntegration(BristleSpringIntegration springIntegration) {
    this.springIntegration = springIntegration;
  }

  public void setInitialConfiguration(InitialConfiguration initialConfiguration) {
    this.initialConfiguration = initialConfiguration;
  }
}
