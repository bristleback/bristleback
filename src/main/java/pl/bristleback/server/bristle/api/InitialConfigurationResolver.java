package pl.bristleback.server.bristle.api;

import pl.bristleback.server.bristle.conf.InitialConfiguration;

/**
 * Implementations of this interface resolve configuration settings from source provided by application creator.
 * <p/>
 * Created on: 2011-09-26 22:15:58 <br/>
 *
 * @author Wojciech Niemiec
 */
public interface InitialConfigurationResolver {

  /**
   * Resolves configuration settings.
   * This method should return complete configuration settings object.
   *
   * @return configuration settings object.
   */
  InitialConfiguration resolveConfiguration();
} 
