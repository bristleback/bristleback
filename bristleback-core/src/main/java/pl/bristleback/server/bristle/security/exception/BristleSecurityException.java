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
 * Base Bristleback Security System exception, extended by more precise exceptions.
 * In almost every exception situation, objects of this class contain information about username,
 * to which this exception is addressed.
 * <p/>
 * Created on: 30.03.13 09:05 <br/>
 *
 * @author Wojciech Niemiec
 */
public class BristleSecurityException extends RuntimeException {

  private String username;

  public BristleSecurityException() {
  }

  public BristleSecurityException(String username) {
    this.username = username;
  }

  public BristleSecurityException(String username, String message) {
    super(message);
    this.username = username;
  }

  public BristleSecurityException(String username, String message, Throwable cause) {
    super(message, cause);
    this.username = username;
  }

  public String getUsername() {
    return username;
  }
}
