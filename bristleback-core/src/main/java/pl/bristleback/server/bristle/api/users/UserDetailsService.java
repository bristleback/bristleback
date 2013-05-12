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
 * Components implementing this interface resolve {@link UserDetails} objects from any source chosen by the implementation.
 * User details services should not validate retrieved user details as this task is later done by authentication actions
 * (actions annotated with {@link pl.bristleback.server.bristle.api.annotations.Authenticator})
 * and {@link pl.bristleback.server.bristle.security.authentication.AuthenticationInterceptor}.
 * <p/>
 * <p/>
 * Created on: 19.02.13 22:44 <br/>
 *
 * @author Wojciech Niemiec
 */
public interface UserDetailsService {

  /**
   * Retrieves {@link UserDetails} implementation for user with given username.
   * If there is no user with given username, service can return null or throw any exception, like
   * {@link pl.bristleback.server.bristle.security.exception.IncorrectUsernameOrPasswordException}.
   *
   * @param username name of the user to retrieve.
   * @return {@link UserDetails} implementation or null if there is no user with given username.
   */
  UserDetails getUserDetails(String username);
}
