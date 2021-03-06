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
 * Exception thrown when user with given name cannot be found or password for that user is not valid.
 * <p/>
 * Created on: 19.02.13 22:47 <br/>
 *
 * @author Wojciech Niemiec
 */
public class IncorrectUsernameOrPasswordException extends BristleSecurityException {

  public IncorrectUsernameOrPasswordException(String username) {
    super(username);
  }
}
