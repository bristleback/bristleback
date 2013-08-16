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

package pl.bristleback.server.bristle.conf;

import org.apache.log4j.Level;
import pl.bristleback.server.bristle.api.users.UserContext;
import pl.bristleback.server.bristle.utils.StringUtils;

import java.util.Set;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-09-26 22:19:30 <br/>
 *
 * @author Wojciech Niemiec
 */
public class InitialConfiguration {

  public static final String DEFAULT_LOGGING_LEVEL = StringUtils.EMPTY;

  public static final String DEFAULT_ENGINE_NAME = "system.engine.jetty";

  public static final int DEFAULT_ENGINE_PORT = 8765;

  public static final int DEFAULT_MAX_BUFFER_SIZE = 65536;

  public static final int DEFAULT_ENGINE_TIMEOUT = 1000 * 60 * 5; // 5 minutes

  public static final int DEFAULT_MAX_FRAME_SIZE = 65536;

  public static final String DEFAULT_DATA_CONTROLLER = "system.controller.action";

  public static final String DEFAULT_SERIALIZATION_ENGINE = "system.serializer.jackson";

  public static final String DEFAULT_MESSAGE_DISPATCHER = "system.dispatcher.multi.threaded";

  private static final String BRISTLEBACK_ROOT_PACKAGE = "pl.bristleback.server";

  public static final String[] SYSTEM_BASE_PACKAGES = {BRISTLEBACK_ROOT_PACKAGE};

  private Set<String> acceptedControllerNames;

  private String defaultControllerName;

  private String serializationEngine;

  private EngineConfig engineConfiguration;

  private String messageDispatcher;

  private Level loggingLevel;

  private BristlebackComponentsContainer springIntegration;

  private Class<? extends UserContext> userContextClass;

  /**
   * Factory class that provides new user object
   * If not specified, {@link pl.bristleback.server.bristle.engine.user.BaseUserContext} object will be created
   */
  private String userContextFactory;

  public Set<String> getAcceptedControllerNames() {
    return acceptedControllerNames;
  }

  public void setAcceptedControllerNames(Set<String> acceptedControllerNames) {
    this.acceptedControllerNames = acceptedControllerNames;
  }

  public String getDefaultControllerName() {
    return defaultControllerName;
  }

  public void setDefaultControllerName(String defaultProtocolName) {
    this.defaultControllerName = defaultProtocolName;
  }

  public EngineConfig getEngineConfiguration() {
    return engineConfiguration;
  }

  public void setEngineConfiguration(EngineConfig engineConfiguration) {
    this.engineConfiguration = engineConfiguration;
  }

  public Level getLoggingLevel() {
    return loggingLevel;
  }

  public void setLoggingLevel(Level loggingLevel) {
    this.loggingLevel = loggingLevel;
  }

  public String getSerializationEngine() {
    return serializationEngine;
  }

  public void setSerializationEngine(String serializationEngine) {
    this.serializationEngine = serializationEngine;
  }

  public BristlebackComponentsContainer getSpringIntegration() {
    return springIntegration;
  }

  public void setSpringIntegration(BristlebackComponentsContainer springIntegration) {
    this.springIntegration = springIntegration;
  }

  public String getMessageDispatcher() {
    return messageDispatcher;
  }

  public void setMessageDispatcher(String messageDispatcher) {
    this.messageDispatcher = messageDispatcher;
  }

  public Class<? extends UserContext> getUserContextClass() {
    return userContextClass;
  }

  public void setUserContextClass(Class<? extends UserContext> userContextClass) {
    this.userContextClass = userContextClass;
  }

  public String getUserContextFactory() {
    return userContextFactory;
  }

  public void setUserContextFactory(String userContextFactory) {
    this.userContextFactory = userContextFactory;
  }
}