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

import pl.bristleback.server.bristle.api.users.UserContext;
import pl.bristleback.server.bristle.api.users.UserContextFactory;
import pl.bristleback.server.bristle.security.UsersContainer;

/**
 * //@todo class description
 * <p/>
 * Created on: 17.02.13 14:02 <br/>
 *
 * @author Wojciech Niemiec
 */
public class UserConfiguration {

  private Class<? extends UserContext> userContextClass;

  private UserContextFactory userContextFactory;

  private UsersContainer usersContainer;


  public UserConfiguration(UserContextFactory userContextFactory, Class<? extends UserContext> userContextClass, UsersContainer usersContainer) {
    this.userContextFactory = userContextFactory;
    this.userContextClass = userContextClass;
    this.usersContainer = usersContainer;
  }

  public Class<? extends UserContext> getUserContextClass() {
    return userContextClass;
  }

  public UserContextFactory getUserContextFactory() {
    return userContextFactory;
  }

  public UsersContainer getUsersContainer() {
    return usersContainer;
  }
}
