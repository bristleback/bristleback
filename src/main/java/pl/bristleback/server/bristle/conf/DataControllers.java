package pl.bristleback.server.bristle.conf;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import pl.bristleback.server.bristle.api.DataController;
import pl.bristleback.server.bristle.exceptions.BristleRuntimeException;

import java.util.Map;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-04-23 22:17:19 <br/>
 *
 * @author Wojciech Niemiec
 */
public class DataControllers {
  private static Logger log = Logger.getLogger(DataControllers.class.getName());

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
