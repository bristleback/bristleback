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

package pl.bristleback.server.bristle.security.exception;

/**
 * This simple exception wraps all situations when username and password are correct but
 * authentication process cannot be completed because of one of the following issues:
 * <ul>
 *   <li>user account is locked ({@link pl.bristleback.server.bristle.api.users.UserDetails#isAccountNonLocked userDetails.isAccountNonLocked()} returns false)</li>
 *   <li>user account has expired ({@link pl.bristleback.server.bristle.api.users.UserDetails#isAccountNonExpired userDetails.isAccountNonExpired()} returns false)</li>
 *   <li>user account is not enabled ({@link pl.bristleback.server.bristle.api.users.UserDetails#isEnabled userDetails.isEnabled()} returns false)</li>
 *   <li>user credentials have expired ({@link pl.bristleback.server.bristle.api.users.UserDetails#isCredentialsNonExpired userDetails.isCredentialsNonExpired()} returns false)</li>
 * </ul>
 * <p/>
 * Created on: 23.02.13 08:44 <br/>
 *
 * @author Wojciech Niemiec
 */
public class InactiveUserException extends BristleSecurityException {

  public InactiveUserException(String username) {
    super(username);
  }
}
