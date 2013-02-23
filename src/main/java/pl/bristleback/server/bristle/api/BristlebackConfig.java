package pl.bristleback.server.bristle.api;

import pl.bristleback.server.bristle.conf.DataControllers;
import pl.bristleback.server.bristle.conf.InitialConfiguration;
import pl.bristleback.server.bristle.conf.MessageConfiguration;
import pl.bristleback.server.bristle.conf.UserConfiguration;
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

  /**
   * Gets initial configuration used to resolve this configuration.
   *
   * @return initial configuration.
   */
  InitialConfiguration getInitialConfiguration();

  /**
   * Gets message configuration, containing message dispatcher.
   *
   * @return message configuration.
   */
  MessageConfiguration getMessageConfiguration();

  /**
   * Gets server engine.
   *
   * @return server engine.
   */
  ServerEngine getServerEngine();

  /**
   * Gets active data controller with name passed as parameter.
   *
   * @param controllerName data controller name.
   * @return active data controller.
   */
  DataController getDataController(String controllerName);

  /**
   * Gets all active data controllers.
   *
   * @return all active data controllers.
   */
  DataControllers getDataControllers();

  /**
   * Gets application state listeners container.
   *
   * @return application state listeners container.
   */
  ListenersContainer getListenersContainer();

  /**
   * Gets serialization engine.
   *
   * @return serialization engine.
   */
  SerializationEngine getSerializationEngine();

  /**
   * Gets Spring integration, containing actual application context and Bristleback internal application context.
   *
   * @return Spring application contexts container.
   */
  BristleSpringIntegration getSpringIntegration();

  UserConfiguration getUserConfiguration();
}
