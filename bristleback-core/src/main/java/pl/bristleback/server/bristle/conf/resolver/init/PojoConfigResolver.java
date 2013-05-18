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

import org.apache.log4j.Level;
import org.springframework.util.Assert;
import pl.bristleback.server.bristle.api.InitialConfigurationResolver;
import pl.bristleback.server.bristle.api.users.UserContext;
import pl.bristleback.server.bristle.conf.EngineConfig;
import pl.bristleback.server.bristle.conf.InitialConfiguration;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * This configuration resolver simply contains setter methods for each configuration setting.
 * Default values are taken from {@link pl.bristleback.server.bristle.conf.resolver.init.DefaultConfigurationResolver DefaultConfigurationResolver} instance.
 * <p/>
 * Created on: 2012-01-31 17:16:26 <br/>
 *
 * @author Wojciech Niemiec
 */
public class PojoConfigResolver implements InitialConfigurationResolver {

  private InitialConfiguration initialConfiguration;

  /**
   * Creates an instance of initial configuration with filled default configuration values.
   */
  public PojoConfigResolver() {
    InitialConfigurationResolver defaultConfigurationResolver = new DefaultConfigurationResolver();
    initialConfiguration = defaultConfigurationResolver.resolveConfiguration();
  }

  @Override
  public InitialConfiguration resolveConfiguration() {
    return initialConfiguration;
  }

  /**
   * Sets acceptable controller names. Each client chooses one data controller from list of available controllers.
   * Data controllers must implement {@link pl.bristleback.server.bristle.api.DataController} interface.
   *
   * @param acceptedControllerNames names of controllers to activate.
   */
  public void setAcceptedControllerNames(String... acceptedControllerNames) {
    assertThatArrayIsNotEmpty(acceptedControllerNames);
    initialConfiguration.setAcceptedControllerNames(new HashSet<String>(Arrays.asList(acceptedControllerNames)));
    initialConfiguration.setDefaultControllerName(acceptedControllerNames[0]);
  }

  /**
   * Sets logging level used in Bristleback application. Values should be taken from {@link org.apache.log4j.Level} fields.
   *
   * @param loggingLevel logging level used in Bristleback application.
   */
  public void setLoggingLevel(String loggingLevel) {
    initialConfiguration.setLoggingLevel(Level.toLevel(loggingLevel));
  }

  /**
   * Sets engine used in all serialization and deserialization operations. Serialization engine must implement
   * {@link pl.bristleback.server.bristle.api.SerializationEngine} interface.
   *
   * @param serializationEngine serialization engine implementation.
   */
  public void setSerializationEngine(String serializationEngine) {
    initialConfiguration.setSerializationEngine(serializationEngine);
  }

  /**
   * Sets server engine. Server engine must implement {@link pl.bristleback.server.bristle.api.ServerEngine} interface.
   *
   * @param engineName server engine.
   */
  public void setEngineName(String engineName) {
    EngineConfig engineConfig = initialConfiguration.getEngineConfiguration();
    engineConfig.setName(engineName);
  }

  /**
   * Sets port on which server engine will listen for new connections.
   * In {@link pl.bristleback.server.bristle.api.ServletServerEngine ServletServerEngine} implementations,
   * engine port may be determined by the web server used.
   *
   * @param enginePort engine port.
   */
  public void setEnginePort(int enginePort) {
    EngineConfig engineConfig = initialConfiguration.getEngineConfiguration();
    engineConfig.setPort(enginePort);
  }

  /**
   * Sets maximum connection idle time (in milliseconds), after which the connection will be closed.
   *
   * @param timeout maximum connection idle time.
   */
  public void setEngineTimeout(int timeout) {
    EngineConfig engineConfig = initialConfiguration.getEngineConfiguration();
    engineConfig.setTimeout(timeout);
  }

  /**
   * Sets maximum message size.
   *
   * @param maxMessageSize maximum message size.
   */
  public void setEngineMaxMessageSize(int maxMessageSize) {
    EngineConfig engineConfig = initialConfiguration.getEngineConfiguration();
    engineConfig.setMaxFrameSize(maxMessageSize);
  }

  /**
   * Sets additional engine properties.
   *
   * @param properties additional engine properties.
   */
  public void setEngineProperties(Map<String, String> properties) {
    EngineConfig engineConfig = initialConfiguration.getEngineConfiguration();
    engineConfig.setProperties(properties);
  }

  /**
   * This property is currently not used.
   *
   * @param rejectedDomains rejected domain names.
   */
  public void setEngineRejectedDomains(List<String> rejectedDomains) {
    EngineConfig engineConfig = initialConfiguration.getEngineConfiguration();
    engineConfig.setRejectedDomains(rejectedDomains);
  }

  /**
   * Sets maximum server engine buffer size for each connector.
   *
   * @param bufferSize maximum buffer size.
   */
  public void setMaxBufferSize(int bufferSize) {
    EngineConfig engineConfig = initialConfiguration.getEngineConfiguration();
    engineConfig.setMaxBufferSize(bufferSize);
  }

  public void setUserContextClass(Class<? extends UserContext> userContextClass) {
    initialConfiguration.setUserContextClass(userContextClass);
  }

  /**
   * Sets the name of user factory bean that will be used to resolve {@link pl.bristleback.server.bristle.api.users.UserContext UserContext}
   * implementations on connection start. User context factory must implement {@link pl.bristleback.server.bristle.api.users.UserContextFactory} interface.
   *
   * @param userContextFactory user context factory.
   */
  public void setUserContextFactory(String userContextFactory) {
    initialConfiguration.setUserContextFactory(userContextFactory);
  }

  private void assertThatArrayIsNotEmpty(String... parameters) {
    Assert.notEmpty(parameters, "Exception while resolving initial configuration. \n"
      + "Empty array is not allowed as the configuration parameter value.");
  }
}
