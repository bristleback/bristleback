/*
 * Bristleback Websocket Framework - Copyright (c) 2010-2013 http://bristleback.pl
 * ---------------------------------------------------------------------------
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation; either version 3 of the License, or (at your
 * option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but without any warranty; without even the implied warranty of merchantability
 * or fitness for a particular purpose.
 * You should have received a copy of the GNU Lesser General Public License along
 * with this program; if not, see <http://www.gnu.org/licenses/lgpl.html>.
 * ---------------------------------------------------------------------------
 */

package pl.bristleback.server.bristle.conf.resolver;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.bristleback.server.bristle.api.ConnectionStateListener;
import pl.bristleback.server.bristle.api.DataController;
import pl.bristleback.server.bristle.api.MessageDispatcher;
import pl.bristleback.server.bristle.api.SerializationEngine;
import pl.bristleback.server.bristle.api.ServerEngine;
import pl.bristleback.server.bristle.api.users.UserContext;
import pl.bristleback.server.bristle.api.users.UserContextFactory;
import pl.bristleback.server.bristle.conf.BristleConfig;
import pl.bristleback.server.bristle.conf.BristleInitializationException;
import pl.bristleback.server.bristle.conf.DataControllers;
import pl.bristleback.server.bristle.conf.EngineConfig;
import pl.bristleback.server.bristle.conf.InitialConfiguration;
import pl.bristleback.server.bristle.conf.MessageConfiguration;
import pl.bristleback.server.bristle.conf.UserConfiguration;
import pl.bristleback.server.bristle.conf.resolver.action.IncreasingOrderSorter;
import pl.bristleback.server.bristle.conf.resolver.message.ObjectSenderInitializer;
import pl.bristleback.server.bristle.conf.resolver.message.ObjectSenderInjector;
import pl.bristleback.server.bristle.engine.user.BaseUserContext;
import pl.bristleback.server.bristle.engine.user.DefaultUserContextFactory;
import pl.bristleback.server.bristle.integration.spring.BristleSpringIntegration;
import pl.bristleback.server.bristle.listener.ListenersContainer;
import pl.bristleback.server.bristle.message.ConditionObjectSender;
import pl.bristleback.server.bristle.security.UsersContainer;
import pl.bristleback.server.bristle.utils.ReflectionUtils;

import javax.inject.Inject;
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
public class SpringConfigurationResolver {

  public static final String CONFIG_BEAN_NAME = "bristlebackConfiguration";

  @Inject
  private BristleSpringIntegration springIntegration;

  @Inject
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
    sortInIncreasingOrder(connectionStateListeners);
    return new ListenersContainer(connectionStateListeners);
  }

  private void sortInIncreasingOrder(List<ConnectionStateListener> connectionStateListeners) {
    IncreasingOrderSorter<ConnectionStateListener> sorter = createIncreasingOrderSorter();
    sorter.sort(connectionStateListeners);
  }

  private IncreasingOrderSorter<ConnectionStateListener> createIncreasingOrderSorter() {
    return new IncreasingOrderSorter<ConnectionStateListener>() {

      @Override
      public Class<?> getSortedClass(ConnectionStateListener object) {
        return object.getClass();
      }
    };
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
      objectSenderInitializer.initObjectSender(bristlebackConfiguration(), sender);
    }
  }

  private ObjectSenderInitializer objectSenderInitializer() {
    return springIntegration.getFrameworkBean("objectSenderInitializer", ObjectSenderInitializer.class);
  }

  @SuppressWarnings("unchecked")
  @Bean
  public UserConfiguration userConfiguration() {

    UserContextFactory userContextFactory = userContextFactory();
    Class<? extends UserContext> userContextClass =
      (Class<? extends UserContext>) ReflectionUtils.getParameterTypes(userContextFactory().getClass(), UserContextFactory.class)[0];

    UsersContainer usersContainer = springIntegration().getBean(UsersContainer.class);

    return new UserConfiguration(userContextFactory, userContextClass, usersContainer);
  }

  @Bean
  public UserContextFactory userContextFactory() {
    String customUserContextFactoryName = initialConfiguration.getUserContextFactory();

    if (StringUtils.isNotBlank(customUserContextFactoryName)) {
      return springIntegration.getApplicationBean(customUserContextFactoryName, UserContextFactory.class);
    }
    Map<String, UserContextFactory> userContextFactoryBeans = springIntegration.getApplicationBeansOfType(UserContextFactory.class);
    if (userContextFactoryBeans.size() == 0) { //no beans found
      return userContextFactoryUsingContextClass();
    }
    if (userContextFactoryBeans.size() == 1) { //one bean found in application configuration
      return springIntegration.getApplicationBean(UserContextFactory.class);
    } else { //more than one bean found in application configuration (initial configuration doesn't contain property which one to choose
      throw new BristleInitializationException("Found more than one implementation of class"
        + UserContextFactory.class.getName() + ". "
        + "Please specify in initial configuration which one should be used");
    }
  }

  private UserContextFactory userContextFactoryUsingContextClass() {
    Class<? extends UserContext> userContextClass = initialConfiguration.getUserContextClass();
    if (userContextClass == null) {
      userContextClass = BaseUserContext.class;
    }
    return new DefaultUserContextFactory(userContextClass);
  }

  public void setSpringIntegration(BristleSpringIntegration springIntegration) {
    this.springIntegration = springIntegration;
  }
}
