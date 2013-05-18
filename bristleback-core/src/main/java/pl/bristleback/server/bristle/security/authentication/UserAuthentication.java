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

import pl.bristleback.server.bristle.api.users.UserContext;
import pl.bristleback.server.bristle.api.users.UserDetails;

/**
 * Abstraction for single user authentication. Binds connection with user details.
 * <p/>
 * Created on: 17.02.13 10:43 <br/>
 *
 * @author Wojciech Niemiec
 */
public final class UserAuthentication {


  private final UserContext userContext;

  private final UserDetails authenticatedUser;

  private boolean valid;

  private UserAuthentication(UserContext userContext, UserDetails authenticatedUser) {
    this.userContext = userContext;
    this.authenticatedUser = authenticatedUser;
    valid = true;
  }

  /**
   * Creates new valid authentication.
   *
   * @param userContext user context object.
   * @param userDetails details of logged user.
   * @return valid authentication.
   */
  public static UserAuthentication newValidAuthentication(UserContext userContext, UserDetails userDetails) {
    return new UserAuthentication(userContext, userDetails);
  }

  public boolean isValid() {
    return valid;
  }

  public UserDetails getAuthenticatedUser() {
    return authenticatedUser;
  }

  public UserContext getUserContext() {
    return userContext;
  }

  /**
   * Invalidate this authentication, making it not usable in secured actions.
   */
  public void invalidate() {
    valid = false;
  }
}
