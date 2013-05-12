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

package pl.bristleback.server.bristle.engine.user;

import pl.bristleback.server.bristle.api.users.UserContext;
import pl.bristleback.server.bristle.api.users.UserContextFactory;
import pl.bristleback.server.bristle.conf.BristleInitializationException;

/**
 * Provides user object for framework
 * If implementation of {@link pl.bristleback.server.bristle.api.users.UserContextFactory} is not provided by framework user,
 * that class is used and {@link BaseUserContext} object is created.
 * <p/>
 *
 * @author Pawel Machowski
 *         created at 03.05.12 16:34
 */
public class DefaultUserContextFactory implements UserContextFactory<UserContext> {

  private Class<? extends UserContext> userContextClass;

  public DefaultUserContextFactory(Class<? extends UserContext> userContextClass) {
    this.userContextClass = userContextClass;
  }

  @Override
  public UserContext createNewUserContext() {
    try {
      return userContextClass.newInstance();
    } catch (InstantiationException e) {
      throw new BristleInitializationException("Could not create new user context object of type: " + userContextClass.getName(), e);
    } catch (IllegalAccessException e) {
      throw new BristleInitializationException("Could not create new user context object of type: " + userContextClass.getName()
        + " because of illegal access of that user context class.", e);
    }
  }

}
