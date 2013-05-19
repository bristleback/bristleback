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

import org.apache.log4j.Logger;
import pl.bristleback.server.bristle.api.ConnectionStateListener;
import pl.bristleback.server.bristle.api.users.UserContext;
import pl.bristleback.server.bristle.listener.ConnectionStateListenerChain;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * //@todo class description
 * <p/>
 * Created on: 14.03.13 13:18 <br/>
 *
 * @author Wojciech Niemiec
 */
public class UserDisconnectedListener implements ConnectionStateListener<UserContext> {

  private static Logger log = Logger.getLogger(UserDisconnectedListener.class.getName());

  @Inject
  @Named("bristleAuthenticationsContainer")
  private AuthenticationsContainer authenticationsContainer;

  @Override
  public void userConnected(UserContext userContext, ConnectionStateListenerChain connectionStateListenerChain) {

  }

  @Override
  public void userDisconnected(UserContext userContext, ConnectionStateListenerChain connectionStateListenerChain) {
    if (authenticationsContainer.hasValidAuthenticationForConnection(userContext.getId())) {
      String username = authenticationsContainer.getAuthentication(userContext.getId()).getAuthenticatedUser().getUsername();
      authenticationsContainer.logout(userContext.getId());
      log.debug("User \"" + username + "\" has been logged out.");
    }
  }
}
