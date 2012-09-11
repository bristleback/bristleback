package pl.bristleback.server.bristle.conf;

import org.apache.log4j.Level;
import pl.bristleback.server.bristle.integration.spring.BristleSpringIntegration;

import java.util.Set;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-09-26 22:19:30 <br/>
 *
 * @author Wojciech Niemiec
 */
public class InitialConfiguration {

  public static final String DEFAULT_LOGGING_LEVEL = "DEBUG";
  public static final String DEFAULT_ENGINE_NAME = "system.engine.jetty";
  public static final int DEFAULT_ENGINE_PORT = 8765;
  public static final int DEFAULT_MAX_BUFFER_SIZE = 65536;
  public static final int DEFAULT_ENGINE_TIMEOUT = 1000 * 60 * 5; // 5 minutes
  public static final int DEFAULT_MAX_FRAME_SIZE = 65536;
  public static final String DEFAULT_DATA_CONTROLLER = "system.controller.action";
  public static final String DEFAULT_SERIALIZATION_ENGINE = "system.serializer.json";
  public static final String DEFAULT_MESSAGE_DISPATCHER = "system.dispatcher.single.threaded";


  private static final String BRISTLEBACK_ROOT_PACKAGE = "pl.bristleback.server";
  public static final String[] SYSTEM_BASE_PACKAGES = {BRISTLEBACK_ROOT_PACKAGE};

  private Set<String> acceptedControllerNames;
  private String defaultControllerName;
  private String serializationEngine;

  private EngineConfig engineConfiguration;

  private String messageDispatcher;

  private Level loggingLevel;

  private BristleSpringIntegration springIntegration;

  /**
   * Factory class that provides new user object
   * If not specified, {@link pl.bristleback.server.bristle.engine.base.users.DefaultUser} object will be created
   */
  private String userFactory;


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

  public BristleSpringIntegration getSpringIntegration() {
    return springIntegration;
  }

  public void setSpringIntegration(BristleSpringIntegration springIntegration) {
    this.springIntegration = springIntegration;
  }

  public String getMessageDispatcher() {
    return messageDispatcher;
  }

  public void setMessageDispatcher(String messageDispatcher) {
    this.messageDispatcher = messageDispatcher;
  }

  public void setUserFactory(String userFactory) {
    this.userFactory = userFactory;
  }

  public String getUserFactory() {
    return userFactory;
  }
}