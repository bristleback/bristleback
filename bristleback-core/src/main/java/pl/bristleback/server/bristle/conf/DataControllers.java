package pl.bristleback.server.bristle.conf;

import org.apache.commons.lang.StringUtils;
import pl.bristleback.server.bristle.BristleRuntimeException;
import pl.bristleback.server.bristle.api.DataController;

import java.util.Map;

/**
 * Container of data controllers.
 * <p/>
 * Created on: 2012-04-23 22:17:19 <br/>
 *
 * @author Wojciech Niemiec
 */
public class DataControllers {

  private Map<String, DataController> dataControllers;

  private DataController defaultController;

  public DataControllers(Map<String, DataController> dataControllers, DataController defaultController) {
    this.dataControllers = dataControllers;
    this.defaultController = defaultController;
  }

  public boolean hasController(String controllerName) {
    return dataControllers.containsKey(controllerName);
  }

  public DataController getDataController(String controllerName) {
    if (StringUtils.isEmpty(controllerName)) {
      return defaultController;
    }
    DataController controller = dataControllers.get(controllerName);
    if (controller == null) {
      throw new BristleRuntimeException("Cannot find data controller for name: " + controllerName);
    }
    return controller;
  }

  public DataController getDefaultController() {
    return defaultController;
  }
}
