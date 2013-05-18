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

package pl.bristleback.server.bristle.engine.base;

import pl.bristleback.server.bristle.api.WebsocketConnector;
import pl.bristleback.server.bristle.api.users.UserContext;

/**
 * Class used to manage connection between {@link UserContext} and {@link WebsocketConnector}.
 * Represents single user connected to BristleBack server.
 * <p/>
 * Pawel Machowski
 * created at 03.05.12 16:36
 */
public class ConnectedUser {

  private UserContext userContext;

  private WebsocketConnector connector;

  public ConnectedUser(UserContext userContext, WebsocketConnector connector) {
    this.userContext = userContext;
    this.connector = connector;
  }

  public UserContext getUserContext() {
    return userContext;
  }

  public WebsocketConnector getConnector() {
    return connector;
  }
}
