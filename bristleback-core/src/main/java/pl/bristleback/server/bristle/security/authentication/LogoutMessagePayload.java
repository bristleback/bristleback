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

package pl.bristleback.server.bristle.security.authentication;

/**
 * Logout message payload, containing information about username and logout reason.
 * <p/>
 * Created on: 07.04.13 19:31 <br/>
 *
 * @author Wojciech Niemiec
 */
public class LogoutMessagePayload {

  private String username;

  private LogoutReason logoutReason;

  public LogoutMessagePayload(String username, LogoutReason logoutReason) {
    this.username = username;
    this.logoutReason = logoutReason;
  }

  public String getUsername() {
    return username;
  }

  public LogoutReason getLogoutReason() {
    return logoutReason;
  }
}
