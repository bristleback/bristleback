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

package pl.bristleback.server.bristle.api.users;

/**
 * Factory interface used by bristleback framework to create new instance of user context. When new WebSocket connection is
 * opened it creates new instance of {@link UserContext}. If you want to use your own implementation of user context object
 * you should implement this interface and specify created class in configuration. If there is no implementation of
 * UserContextFactory, {@link pl.bristleback.server.bristle.engine.user.DefaultUserContextFactory} will be used by framework.
 *
 * @author Pawel Machowski created at 03.06.12 16:34
 */
public interface UserContextFactory<T extends UserContext> {

  /**
   * Creates new instance of user context object. For every new WebSocket connection it should return unique instance of  {@link
   * UserContext} implementation.
   *
   * @return new instance of user object.
   */
  T createNewUserContext();

}
