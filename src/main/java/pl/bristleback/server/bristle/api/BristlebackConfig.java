package pl.bristleback.server.bristle.api;

import pl.bristleback.server.bristle.conf.DataControllers;
import pl.bristleback.server.bristle.conf.InitialConfiguration;
import pl.bristleback.server.bristle.conf.MessageConfiguration;
import pl.bristleback.server.bristle.integration.spring.BristleSpringIntegration;
import pl.bristleback.server.bristle.listener.ListenersContainer;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-01-23 19:55:22 <br/>
 *
 * @author Wojciech Niemiec
 */
public interface BristlebackConfig {

  InitialConfiguration getInitialConfiguration();

  MessageConfiguration getMessageConfiguration();

  ServerEngine getServerEngine();

  DataController getDefaultDataController();

  DataController getDataController(String controllerName);

  DataControllers getDataControllers();

  ListenersContainer getListenersContainer();

  SerializationEngine getSerializationEngine();

  BristleSpringIntegration getSpringIntegration();
}
