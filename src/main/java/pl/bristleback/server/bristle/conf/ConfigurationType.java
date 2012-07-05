package pl.bristleback.server.bristle.conf;

import pl.bristleback.server.bristle.api.MessageDispatcher;
import pl.bristleback.server.bristle.exceptions.BristleInitializationException;
import pl.bristleback.server.bristle.message.SingleThreadMessageDispatcher;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-07-05 20:23:36 <br/>
 *
 * @author Wojciech Niemiec
 */
public enum ConfigurationType {

  MESSAGE_DISPATCHER(MessageDispatcher.class, SingleThreadMessageDispatcher.class);

  private Class interfaceToImplement;
  private Class defaultImplementationClass;


  ConfigurationType(Class interfaceToImplement, Class defaultImplementationClass) {
    this.interfaceToImplement = interfaceToImplement;
    this.defaultImplementationClass = defaultImplementationClass;
  }

  public Class getInterfaceToImplement() {
    return interfaceToImplement;
  }

  public Class getDefaultImplementationClass() {
    return defaultImplementationClass;
  }


  public static <T> Class<T> getDefaultConfiguration(Class<T> interfaceToImplement) {
    for (ConfigurationType configurationType : ConfigurationType.values()) {
      if (configurationType.interfaceToImplement.equals(interfaceToImplement)) {
        return configurationType.defaultImplementationClass;
      }
    }
    throw new BristleInitializationException("No configuration element found for type: " + interfaceToImplement);
  }
}
