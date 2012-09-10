package pl.bristleback.server.bristle.conf.resolver.init;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.util.Assert;
import pl.bristleback.server.bristle.api.InitialConfigurationResolver;
import pl.bristleback.server.bristle.conf.EngineConfig;
import pl.bristleback.server.bristle.conf.InitialConfiguration;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-01-31 17:16:26 <br/>
 *
 * @author Wojciech Niemiec
 */
public class PojoConfigResolver implements InitialConfigurationResolver {
  private static Logger log = Logger.getLogger(PojoConfigResolver.class.getName());

  private InitialConfiguration initialConfiguration;

  public PojoConfigResolver() {
    InitialConfigurationResolver defaultConfigurationResolver = new DefaultConfigurationResolver();
    initialConfiguration = defaultConfigurationResolver.resolveConfiguration();
  }

  @Override
  public InitialConfiguration resolveConfiguration() {
    return initialConfiguration;
  }

  public void setAcceptedControllerNames(String... acceptedControllerNames) {
    assertThatArrayIsNotEmpty(acceptedControllerNames);
    initialConfiguration.setAcceptedControllerNames(new HashSet<String>(Arrays.asList(acceptedControllerNames)));
    initialConfiguration.setDefaultControllerName(acceptedControllerNames[0]);
  }

  public void setLoggingLevel(String loggingLevel) {
    initialConfiguration.setLoggingLevel(Level.toLevel(loggingLevel));
  }

  public void setSerializationEngine(String serializationEngine) {
    initialConfiguration.setSerializationEngine(serializationEngine);
  }

  public void setEngineName(String engineName) {
    EngineConfig engineConfig = initialConfiguration.getEngineConfiguration();
    engineConfig.setName(engineName);
  }

  public void setEnginePort(int enginePort) {
    EngineConfig engineConfig = initialConfiguration.getEngineConfiguration();
    engineConfig.setPort(enginePort);
  }

  public void setEngineTimeout(int timeout) {
    EngineConfig engineConfig = initialConfiguration.getEngineConfiguration();
    engineConfig.setTimeout(timeout);
  }

  public void setEngineMaxFrameSize(int maxFrameSize) {
    EngineConfig engineConfig = initialConfiguration.getEngineConfiguration();
    engineConfig.setMaxFrameSize(maxFrameSize);
  }

  public void setEngineProperties(Map<String, String> properties) {
    EngineConfig engineConfig = initialConfiguration.getEngineConfiguration();
    engineConfig.setProperties(properties);
  }

  public void setEngineRejectedDomains(List<String> rejectedDomains) {
    EngineConfig engineConfig = initialConfiguration.getEngineConfiguration();
    engineConfig.setRejectedDomains(rejectedDomains);
  }

  public void setMaxBufferSize(int bufferSize) {
    EngineConfig engineConfig = initialConfiguration.getEngineConfiguration();
    engineConfig.setMaxBufferSize(bufferSize);
  }

  public void setUserFactory(String userFactory) {
    initialConfiguration.setUserFactory(userFactory);
  }

  private void assertThatArrayIsNotEmpty(String... parameters) {
    Assert.notEmpty(parameters, "Exception while resolving initial configuration. \n"
      + "Empty array is not allowed as the configuration parameter value.");
  }
}
