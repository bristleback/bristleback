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

import pl.bristleback.server.bristle.security.exception.AuthorizationException;

/**
 * Authorization exception response is a extension for {@link SecurityExceptionResponse} response,
 * adds information which authority is missing. This information is stored in <code>authority</code> field.
 * <p/>
 * Created on: 30.03.13 14:13 <br/>
 *
 * @author Wojciech Niemiec
 */
public class AuthorizationExceptionResponse extends SecurityExceptionResponse {

  private String authority;

  /**
   * Creates new instance of authorization exception response, containing information about username and missing authority.
   *
   * @param username  ame of the user to which this exception response is addressed.
   * @param authority missing authority.
   */
  public AuthorizationExceptionResponse(String username, String authority) {
    super(username, AuthorizationException.class.getSimpleName());
    this.authority = authority;
  }

  public String getAuthority() {
    return authority;
  }
}
