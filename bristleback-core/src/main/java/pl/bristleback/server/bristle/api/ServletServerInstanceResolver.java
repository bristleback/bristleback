package pl.bristleback.server.bristle.api;

import pl.bristleback.server.bristle.app.BristlebackServerInstance;
import pl.bristleback.server.bristle.app.servlet.BristlebackServletInitParameters;

/**
 * Implementations of this interface resolve configuration settings
 * from source provided by application creator and create {@link BristlebackServerInstance},
 * using Java Servlet init parameters.
 * <p/>
 * Created on: 2013-11-17 22:15:58 <br/>
 *
 * @author Wojciech Niemiec
 */
public interface ServletServerInstanceResolver {

  /**
   * Resolves Bristleback server instance using servlet init parameters.
   * Init parameters object is created by retrieving parameters from {@link javax.servlet.ServletContext}.
   * This method should return complete configuration settings object.
   *
   * @param initParameters servlet initialization parameters, taken from {@link javax.servlet.ServletContext}.
   * @return ready to use Bristleback server instance
   */
  BristlebackServerInstance createServerInstance(BristlebackServletInitParameters initParameters);
}
