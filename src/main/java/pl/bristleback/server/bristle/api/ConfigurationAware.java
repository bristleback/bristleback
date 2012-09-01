package pl.bristleback.server.bristle.api;

/**
 * This marker interface indicates that given implementing component (or extending interface)
 * requires Bristleback configuration.
 * <p/>
 * Created on: 2011-09-18 16:13:31 <br/>
 *
 * @author Wojciech Niemiec
 */
public interface ConfigurationAware {

  void init(BristlebackConfig configuration);
} 
