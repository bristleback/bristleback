package pl.bristleback.server.bristle.conf.resolver.init;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import pl.bristleback.server.bristle.api.InitialConfigurationResolver;
import pl.bristleback.server.bristle.conf.EngineConfig;
import pl.bristleback.server.bristle.conf.InitialConfiguration;
import pl.bristleback.server.bristle.utils.ListUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-02-01 21:47:09 <br/>
 *
 * @author Wojciech Niemiec
 */
public class DefaultConfigurationResolver implements InitialConfigurationResolver {
  private static Logger log = Logger.getLogger(DefaultConfigurationResolver.class.getName());

  private List<String> basePackages;

  @Override
  public InitialConfiguration resolveConfiguration() {
    if (ListUtils.isEmpty(basePackages)) {
      throw new IllegalStateException("Base packages not set");
    }
    return resolveDefaultConfiguration();
  }

  public InitialConfiguration resolveDefaultConfiguration() {
    InitialConfiguration initialConfiguration = new InitialConfiguration();
    initialConfiguration.setAcceptedControllerNames(prepareSet(InitialConfiguration.DEFAULT_DATA_CONTROLLER));
    initialConfiguration.setDefaultControllerName(InitialConfiguration.DEFAULT_DATA_CONTROLLER);
    initialConfiguration.setLoggingLevel(Level.toLevel(InitialConfiguration.DEFAULT_LOGGING_LEVEL));
    initialConfiguration.setSerializationEngine(InitialConfiguration.DEFAULT_SERIALIZATION_ENGINE);
    initialConfiguration.setMessageDispatcher(InitialConfiguration.DEFAULT_MESSAGE_DISPATCHER);
    prepareEngineConfiguration(initialConfiguration);

    return initialConfiguration;
  }

  private void prepareEngineConfiguration(InitialConfiguration initialConfiguration) {
    EngineConfig engineConfiguration = new EngineConfig();
    engineConfiguration.setName(InitialConfiguration.DEFAULT_ENGINE_NAME);
    engineConfiguration.setMaxBufferSize(InitialConfiguration.DEFAULT_MAX_BUFFER_SIZE);
    engineConfiguration.setMaxFrameSize(InitialConfiguration.DEFAULT_MAX_FRAME_SIZE);
    engineConfiguration.setPort(InitialConfiguration.DEFAULT_ENGINE_PORT);
    engineConfiguration.setTimeout(InitialConfiguration.DEFAULT_ENGINE_TIMEOUT);
    engineConfiguration.setProperties(new HashMap<String, String>());
    engineConfiguration.setRejectedDomains(new ArrayList<String>());

    initialConfiguration.setEngineConfiguration(engineConfiguration);
  }

  private Set<String> prepareSet(String value) {
    Set<String> set = new HashSet<String>();
    set.add(value);
    return set;
  }

  public void setBasePackages(String... basePackages) {
    this.basePackages = Arrays.asList(basePackages);
  }
}
