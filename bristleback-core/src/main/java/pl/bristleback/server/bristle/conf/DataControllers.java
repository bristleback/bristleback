/*
 * Bristleback Websocket Framework - Copyright (c) 2010-2013 http://bristleback.pl
 * ---------------------------------------------------------------------------
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation; either version 3 of the License, or (at your
 * option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but without any warranty; without even the implied warranty of merchantability
 * or fitness for a particular purpose.
 * You should have received a copy of the GNU Lesser General Public License along
 * with this program; if not, see <http://www.gnu.org/licenses/lgpl.html>.
 * ---------------------------------------------------------------------------
 */

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
