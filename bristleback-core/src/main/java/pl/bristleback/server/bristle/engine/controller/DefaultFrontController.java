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

package pl.bristleback.server.bristle.engine.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.api.FrontController;
import pl.bristleback.server.bristle.api.WebsocketConnector;
import pl.bristleback.server.bristle.security.UsersContainer;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * This implementation of front controller can handle three kinds of messages:
 * <ul>
 * <li>Text message</li>
 * <li>Binary message</li>
 * <li>Close connection message</li>
 * </ul>
 * Future version should bring possibility to handle ping/pong messages.
 * <p/>
 * Created on: 2011-11-21 18:35:01 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component("defaultFrontController")
public class DefaultFrontController implements FrontController {

  private static Logger log = Logger.getLogger(DefaultFrontController.class.getName());

  private Map<Integer, WebsocketOperation> operations;

  @Inject
  private UsersContainer usersContainer;

  public DefaultFrontController() {
    operations = new HashMap<Integer, WebsocketOperation>();
    for (WebsocketOperation operation : WebsocketOperation.values()) {
      operations.put(operation.getOperationCode().getCode(), operation);
    }
  }

  public void processCommand(WebsocketConnector connector, int operationCode, Object data) {
    WebsocketOperation operation = operations.get(operationCode);
    if (operation != null) {
      operation.performOperation(connector, usersContainer, data);
    } else {
      log.warn("Cannot perform operation with code " + operationCode + ", operation cannot be found.");
    }
  }
}
