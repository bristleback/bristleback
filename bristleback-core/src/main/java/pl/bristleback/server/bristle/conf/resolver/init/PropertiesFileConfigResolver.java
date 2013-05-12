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

package pl.bristleback.server.bristle.conf.resolver.init;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;
import pl.bristleback.server.bristle.api.InitialConfigurationResolver;
import pl.bristleback.server.bristle.conf.EngineConfig;
import pl.bristleback.server.bristle.conf.InitialConfiguration;
import pl.bristleback.server.bristle.conf.BristleInitializationException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This configuration resolver uses {@link java.util.Properties} file to retrieve configuration elements.
 * Default values are taken from {@link pl.bristleback.server.bristle.conf.resolver.init.DefaultConfigurationResolver DefaultConfigurationResolver} instance.
 * The table below shows all properties:
 * <table border="1">
 * <thead>
 * <tr>
 * <th>
 * Property
 * </th>
 * <th>
 * Property key
 * </th>
 * <th>
 * Property value type
 * </th>
 * <th>
 * Default value
 * </th>
 * </tr>
 * </thead>
 * <tbody>
 * <tr>
 * <td>
 * Server engine
 * </td>
 * <td>
 * bristle.engine.name
 * </td>
 * <td>
 * {@link pl.bristleback.server.bristle.api.ServerEngine}
 * </td>
 * <td>
 * system.engine.jetty
 * </td>
 * </tr>
 * <tr>
 * <td>
 * Accepted data controllers
 * </td>
 * <td>
 * bristle.websocket.controller
 * </td>
 * <td>
 * {@link pl.bristleback.server.bristle.api.DataController}
 * </td>
 * <td>
 * system.controller.action
 * </td>
 * </tr>
 * <tr>
 * <td>
 * User context object class
 * </td>
 * <td>
 * bristle.user.context.class
 * </td>
 * <td>
 * {@link pl.bristleback.server.bristle.api.users.UserContext}
 * </td>
 * <td>
 * {@link pl.bristleback.server.bristle.engine.user.BaseUserContext}
 * </td>
 * </tr>
 * <tr>
 * <td>
 * User context object factory
 * </td>
 * <td>
 * bristle.user.context.factory
 * </td>
 * <td>
 * {@link pl.bristleback.server.bristle.api.users.UserContextFactory}
 * </td>
 * <td>
 * {@link pl.bristleback.server.bristle.engine.user.DefaultUserContextFactory}
 * </td>
 * </tr>
 * <tr>
 * <td>
 * Message dispatcher
 * </td>
 * <td>
 * bristle.message.dispatcher
 * </td>
 * <td>
 * {@link pl.bristleback.server.bristle.api.MessageDispatcher}
 * </td>
 * <td>
 * system.dispatcher.multi.threaded
 * </td>
 * </tr>
 * <tr>
 * <td>
 * Serialization engine
 * </td>
 * <td>
 * bristle.serialization.engine
 * </td>
 * <td>
 * {@link pl.bristleback.server.bristle.api.SerializationEngine}
 * </td>
 * <td>
 * system.serializer.json
 * </td>
 * </tr>
 * <tr>
 * <td>
 * Logging level
 * </td>
 * <td>
 * bristle.logging.level
 * </td>
 * <td>
 * {@link org.apache.log4j.Level}
 * </td>
 * <td>
 * DEBUG
 * </td>
 * </tr>
 * <tr>
 * <td>
 * Server Engine port
 * </td>
 * <td>
 * bristle.engine.port
 * </td>
 * <td>
 * {@link Integer}
 * </td>
 * <td>
 * 8765
 * </td>
 * </tr>
 * <tr>
 * <td>
 * Server Engine timeout
 * </td>
 * <td>
 * bristle.engine.timeout
 * </td>
 * <td>
 * {@link Integer}
 * </td>
 * <td>
 * 1000 * 60 * 5 milliseconds (5 minutes)
 * </td>
 * </tr>
 * <tr>
 * <td>
 * Server Engine maximum buffer size
 * </td>
 * <td>
 * bristle.engine.max.buffer.size
 * </td>
 * <td>
 * {@link Integer}
 * </td>
 * <td>
 * 65536
 * </td>
 * </tr>
 * <tr>
 * <td>
 * Server Engine maximum message size
 * </td>
 * <td>
 * bristle.engine.max.message.size
 * </td>
 * <td>
 * {@link Integer}
 * </td>
 * <td>
 * 65536
 * </td>
 * </tr>
 * </tbody>
 * </table>
 * <p/>
 * Created on: 2011-07-03 23:42:06 <br/>
 *
 * @author Wojciech Niemiec
 * @see java.util.Properties
 */
public class PropertiesFileConfigResolver implements InitialConfigurationResolver {

  public static final String ACCEPTED_CONTROLLERS = "bristle.websocket.controller";

  public static final String ENGINE_NAME_PROPERTY = "bristle.engine.name";

  public static final String ENGINE_PORT_PROPERTY = "bristle.engine.port";

  public static final String ENGINE_MAX_BUFFER_SIZE_PROPERTY = "bristle.engine.max.buffer.size";

  public static final String ENGINE_MAX_MESSAGE_SIZE_PROPERTY = "bristle.engine.max.message.size";

  public static final String ENGINE_TIMEOUT_PROPERTY = "bristle.engine.timeout";

  public static final String ENGINE_REJECTED_DOMAINS_PROPERTY = "bristle.engine.rejected.domains";

  public static final String LOGGING_LEVEL_PROPERTY = "bristle.logging.level";

  public static final String SERIALIZATION_ENGINE_PROPERTY = "bristle.serialization.engine";

  public static final String MESSAGE_DISPATCHER_PROPERTY = "bristle.message.dispatcher";

  public static final String USER_CONTEXT_PROPERTY = "bristle.user.context.class";

  public static final String USER_CONTEXT_FACTORY_PROPERTY = "bristle.user.context.factory";

  private static final String DEFAULT_CONFIG_FILE_LOCATION = "conf/bristleback.properties";

  private String configurationPath;

  private PropertiesConfiguration propertiesConfiguration;

  @Override
  public InitialConfiguration resolveConfiguration() {
    if (propertiesConfiguration == null) {
      getPropertiesFromFileName();
    }
    InitialConfigurationResolver defaultConfigurationResolver = new DefaultConfigurationResolver();
    InitialConfiguration initialConfiguration = defaultConfigurationResolver.resolveConfiguration();
    setLoggingLevel(initialConfiguration);
    resolveAcceptedProtocolNames(initialConfiguration);
    resolveMessageDispatcher(initialConfiguration);
    resolveSerializationEngine(initialConfiguration);
    resolveEngineConfiguration(initialConfiguration);
    resolveUserContext(initialConfiguration);
    resolveUserContextFactory(initialConfiguration);
    return initialConfiguration;
  }

  private void resolveUserContext(InitialConfiguration initialConfiguration) {
    initialConfiguration.setUserContextFactory(propertiesConfiguration.getString(USER_CONTEXT_PROPERTY));
  }

  private void resolveUserContextFactory(InitialConfiguration initialConfiguration) {
    initialConfiguration.setUserContextFactory(propertiesConfiguration.getString(USER_CONTEXT_FACTORY_PROPERTY));
  }

  private void getPropertiesFromFileName() {
    try {
      if (StringUtils.isBlank(configurationPath)) {
        configurationPath = DEFAULT_CONFIG_FILE_LOCATION;
      }
      propertiesConfiguration = new PropertiesConfiguration(configurationPath);
    } catch (ConfigurationException e) {
      throw new BristleInitializationException("Unexpected " + e.getClass().getSimpleName() + " has occurred while resolving Bristleback properties file", e);
    }
  }

  private void resolveSerializationEngine(InitialConfiguration initialConfiguration) {
    String serializationEngine = propertiesConfiguration.getString(SERIALIZATION_ENGINE_PROPERTY, InitialConfiguration.DEFAULT_SERIALIZATION_ENGINE);
    initialConfiguration.setSerializationEngine(serializationEngine);
  }

  private void setLoggingLevel(InitialConfiguration initialConfiguration) {
    String loggingLevel = propertiesConfiguration.getString(LOGGING_LEVEL_PROPERTY, InitialConfiguration.DEFAULT_LOGGING_LEVEL);
    initialConfiguration.setLoggingLevel(Level.toLevel(loggingLevel));
  }

  @SuppressWarnings("unchecked")
  private void resolveAcceptedProtocolNames(InitialConfiguration configuration) {
    List<String> controllersList = propertiesConfiguration.getList(ACCEPTED_CONTROLLERS, Collections.singletonList(InitialConfiguration.DEFAULT_DATA_CONTROLLER));
    configuration.setDefaultControllerName(controllersList.get(0));
    Set<String> acceptedControllerNames = new HashSet<String>(controllersList);
    configuration.setAcceptedControllerNames(acceptedControllerNames);
  }

  private void resolveMessageDispatcher(InitialConfiguration initialConfiguration) {
    String messageDispatcher = propertiesConfiguration.getString(MESSAGE_DISPATCHER_PROPERTY, InitialConfiguration.DEFAULT_MESSAGE_DISPATCHER);
    initialConfiguration.setMessageDispatcher(messageDispatcher);
  }

  @SuppressWarnings("unchecked")
  private void resolveEngineConfiguration(InitialConfiguration initialConfiguration) {
    EngineConfig engineConfig = new EngineConfig();
    engineConfig.setName(propertiesConfiguration.getString(ENGINE_NAME_PROPERTY, InitialConfiguration.DEFAULT_ENGINE_NAME));
    engineConfig.setPort(propertiesConfiguration.getInt(ENGINE_PORT_PROPERTY, InitialConfiguration.DEFAULT_ENGINE_PORT));
    engineConfig.setTimeout(propertiesConfiguration.getInt(ENGINE_TIMEOUT_PROPERTY, InitialConfiguration.DEFAULT_ENGINE_TIMEOUT));
    engineConfig.setMaxBufferSize(propertiesConfiguration.getInt(ENGINE_MAX_BUFFER_SIZE_PROPERTY, InitialConfiguration.DEFAULT_MAX_BUFFER_SIZE));
    engineConfig.setMaxFrameSize(propertiesConfiguration.getInt(ENGINE_MAX_MESSAGE_SIZE_PROPERTY, InitialConfiguration.DEFAULT_MAX_FRAME_SIZE));
    engineConfig.setRejectedDomains(propertiesConfiguration.getList(ENGINE_REJECTED_DOMAINS_PROPERTY, new ArrayList()));

    initialConfiguration.setEngineConfiguration(engineConfig);
  }

  /**
   * Sets path to properties file, containing configuration elements.
   *
   * @param configurationPath path to properties file.
   */
  public void setConfigurationPath(String configurationPath) {
    this.configurationPath = configurationPath;
  }
}
