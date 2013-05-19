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
 * This class contains configuration used by the system authentication framework.
 * <p/>
 * Created on: 18.02.13 19:14 <br/>
 *
 * @author Wojciech Niemiec
 */
public class AuthenticationConfiguration {

  private int maximumAuthenticationsPerUsername;

  /**
   * Gets maximum concurrent, valid authentications for each username. If not specified by application creator,
   * value of this property is equal to 0, which means there is no limit of concurrent valid authentications.
   *
   * @return maximum concurrent authentications registered for each username.
   */
  public int getMaximumAuthenticationsPerUsername() {
    return maximumAuthenticationsPerUsername;
  }

  /**
   * Sets maximum concurrent authentications registered for each username. If specified value is set to 0,
   * then no limit of concurrent valid authentications is applied.
   *
   * @param maximumAuthenticationsPerUsername
   *         maximum number of concurrent authentications.
   */
  public void setMaximumAuthenticationsPerUsername(int maximumAuthenticationsPerUsername) {
    this.maximumAuthenticationsPerUsername = maximumAuthenticationsPerUsername;
  }
}
