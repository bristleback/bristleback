package pl.bristleback.server.bristle.conf.resolver.init;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import pl.bristleback.server.bristle.api.InitialConfigurationResolver;
import pl.bristleback.server.bristle.conf.EngineConfig;
import pl.bristleback.server.bristle.conf.InitialConfiguration;
import pl.bristleback.server.bristle.exceptions.BristleInitializationException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-07-03 23:42:06 <br/>
 *
 * @author Wojciech Niemiec
 */
public class PropertiesFileConfigResolver implements InitialConfigurationResolver {
  private static Logger log = Logger.getLogger(PropertiesFileConfigResolver.class.getName());

  private static final String DEFAULT_CONFIG_FILE_LOCATION = "conf/bristleback.properties";

  public static final String ACCEPTED_CONTROLLERS = "bristle.websocket.controller";
  public static final String ENGINE_NAME_PROPERTY = "bristle.engine.name";
  public static final String ENGINE_PORT_PROPERTY = "bristle.engine.port";
  public static final String ENGINE_MAX_BUFFER_SIZE_PROPERTY = "bristle.engine.max.buffer.size";
  public static final String ENGINE_MAX_FRAME_SIZE_PROPERTY = "bristle.engine.max.frame.size";
  public static final String ENGINE_TIMEOUT_PROPERTY = "bristle.engine.timeout";
  public static final String ENGINE_REJECTED_DOMAINS_PROPERTY = "bristle.engine.rejected.domains";
  public static final String LOGGING_LEVEL_PROPERTY = "bristle.logging.level";
  public static final String SERIALIZATION_ENGINE_PROPERTY = "bristle.serialization.engine";
  public static final String USER_FACTORY_PROPERTY = "bristle.user.factory";

  private String configurationPath;
  private PropertiesConfiguration propertiesConfiguration;

  @Override
  public InitialConfiguration resolveConfiguration() {
    if (propertiesConfiguration == null) {
      getPropertiesFromFileName();
    }
    InitialConfiguration initialConfiguration = new InitialConfiguration();
    setLoggingLevel(initialConfiguration);
    resolveAcceptedProtocolNames(initialConfiguration);
    resolveSerializationEngine(initialConfiguration);
    resolveEngineConfiguration(initialConfiguration);
    resolveUserFactory(initialConfiguration);
    return initialConfiguration;
  }

  private void resolveUserFactory(InitialConfiguration initialConfiguration) {
    initialConfiguration.setUserFactory(propertiesConfiguration.getString(USER_FACTORY_PROPERTY));
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

  @SuppressWarnings("unchecked")
  private void resolveEngineConfiguration(InitialConfiguration initialConfiguration) {
    EngineConfig engineConfig = new EngineConfig();
    engineConfig.setName(propertiesConfiguration.getString(ENGINE_NAME_PROPERTY, InitialConfiguration.DEFAULT_ENGINE_NAME));
    engineConfig.setPort(propertiesConfiguration.getInt(ENGINE_PORT_PROPERTY, InitialConfiguration.DEFAULT_ENGINE_PORT));
    engineConfig.setTimeout(propertiesConfiguration.getInt(ENGINE_TIMEOUT_PROPERTY, InitialConfiguration.DEFAULT_ENGINE_TIMEOUT));
    engineConfig.setMaxBufferSize(propertiesConfiguration.getInt(ENGINE_MAX_BUFFER_SIZE_PROPERTY, InitialConfiguration.DEFAULT_MAX_BUFFER_SIZE));
    engineConfig.setMaxFrameSize(propertiesConfiguration.getInt(ENGINE_MAX_FRAME_SIZE_PROPERTY, InitialConfiguration.DEFAULT_MAX_FRAME_SIZE));
    engineConfig.setRejectedDomains(propertiesConfiguration.getList(ENGINE_REJECTED_DOMAINS_PROPERTY, new ArrayList()));

    initialConfiguration.setEngineConfiguration(engineConfig);
  }

  public void setConfigurationPath(String configurationPath) {
    this.configurationPath = configurationPath;
  }
}
