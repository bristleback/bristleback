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

import pl.bristleback.server.bristle.api.WebsocketConnector;
import pl.bristleback.server.bristle.api.users.UserContext;
import pl.bristleback.server.bristle.engine.OperationCode;
import pl.bristleback.server.bristle.security.UsersContainer;

/**
 * This is a helper class used by {@link DefaultFrontController} to handle incoming Websocket messages.
 * <p/>
 * Created on: 2011-09-25 11:59:43 <br/>
 *
 * @author Wojciech Niemiec
 */
public enum WebsocketOperation {

  TEXT_FRAME(OperationCode.TEXT_FRAME_CODE) {
    public void performOperation(WebsocketConnector connector, UsersContainer usersContainer, Object data) {
      UserContext userContext = usersContainer.getUserContext(connector);
      connector.getDataController().processTextData((String) data, userContext);
    }
  },

  BINARY_FRAME(OperationCode.BINARY_FRAME_CODE) {
    public void performOperation(WebsocketConnector connector, UsersContainer usersContainer, Object data) {
      UserContext userContext = usersContainer.getUserContext(connector);
      connector.getDataController().processBinaryData((byte[]) data, userContext);
    }
  },

  CLOSE_FRAME(OperationCode.CLOSE_FRAME_CODE) {
    public void performOperation(WebsocketConnector connector, UsersContainer usersContainer, Object data) {
      connector.stop();
    }
  };

  private OperationCode operationCode;

  WebsocketOperation(OperationCode operationCode) {
    this.operationCode = operationCode;
  }

  public OperationCode getOperationCode() {
    return operationCode;
  }

  public abstract void performOperation(WebsocketConnector connector, UsersContainer usersContainer, Object data);

}
