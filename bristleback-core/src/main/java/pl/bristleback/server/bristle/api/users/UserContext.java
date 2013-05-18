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
 * Interface representing user connected to BristleBack server. Provides user friendly abstraction for dealing with low
 * level WebSocket mechanism.
 * <p/>
 * For each WebSocket connection new instance of UserContext is created. All instances off currently connected users
 * are held in {@link pl.bristleback.server.bristle.security.UsersContainer}. Framework user can create own
 * implementation of this interface and create new {@link UserContextFactory} to handle application specific user behaviour.
 * <p/>
 * If there is no implementation of this interface defined by application creator, instance of class {@link pl.bristleback.server.bristle.engine.user.BaseUserContext}
 * will be created.
 *
 * @author Pawel Machowski created at 01.05.12 14:00
 */
public interface UserContext {

  /**
   * Returns unique id number for this connected user.
   *
   * @return id number of connected user.
   */
  String getId();

  /**
   * Sets unique id number for this connected user.
   * This method should <strong>NOT</strong> be called by application creator.
   * Instead, this method is called by Bristleback users container when new connection is established.
   *
   * @param id unique id number for this connected user.
   */
  void setId(String id);
}
