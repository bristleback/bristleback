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

package pl.bristleback.server.bristle.security.exception.response;

import pl.bristleback.server.bristle.action.response.ExceptionResponse;

/**
 * Security exception response is a base response for all security exceptions in Bristleback Security System.
 * <p/>
 * Created on: 30.03.13 09:50 <br/>
 *
 * @author Wojciech Niemiec
 * @see pl.bristleback.server.bristle.security.exception.handler.BristleSecurityExceptionHandler
 */
public class SecurityExceptionResponse extends ExceptionResponse {

  private String username;

  /**
   * Creates new instance of security exception response, containing information about username and simple type of exception.
   *
   * @param username name of the user to which this exception response is addressed.
   * @param type     simple type of the exception class.
   */
  public SecurityExceptionResponse(String username, String type) {
    super(type);
    this.username = username;
  }

  public String getUsername() {
    return username;
  }
}
