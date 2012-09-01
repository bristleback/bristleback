package pl.bristleback.server.bristle.api;

import pl.bristleback.server.bristle.conf.DataControllers;
import pl.bristleback.server.bristle.conf.InitialConfiguration;
import pl.bristleback.server.bristle.conf.MessageConfiguration;
import pl.bristleback.server.bristle.integration.spring.BristleSpringIntegration;
import pl.bristleback.server.bristle.listener.ListenersContainer;

/**
 * Bristleback Server configuration, containing all basic server components.
 * Bristleback configuration should be only used by server internals and components creators.
 * <p/>
 * Created on: 2012-01-23 19:55:22 <br/>
 *
 * @author Wojciech Niemiec
 */
public interface BristlebackConfig {

  InitialConfiguration getInitialConfiguration();

  MessageConfiguration getMessageConfiguration();

  ServerEngine getServerEngine();

  DataController getDataController(String controllerName);

  DataControllers getDataControllers();

  ListenersContainer getListenersContainer();

  SerializationEngine getSerializationEngine();

  BristleSpringIntegration getSpringIntegration();
}
