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

package pl.bristleback.server.bristle.api;

import pl.bristleback.server.bristle.api.users.UserContext;
import pl.bristleback.server.bristle.listener.ConnectionStateListenerChain;

/**
 * This interface is meant to be implemented by application creator to handle user connection and disconnection events.
 * There can be multiple connection state listeners defined, they are run by {@link pl.bristleback.server.bristle.listener.ConnectionStateListenerChain ConnectionStateListenerChain}.
 * Listener methods are parametrized with base type of {@link pl.bristleback.server.bristle.api.users.UserContext}.
 * In all cases, actual user context implementation (according to {@link pl.bristleback.server.bristle.api.users.UserContextFactory UserFactory}
 * used in application) is passed, listeners may be parametrized with custom user implementations so they won't be forced to use casting.
 * The order of connection state listeners execution can be set using Spring Framework {@link org.springframework.core.annotation.Order} annotation.
 * <p/>
 * Created on: 2011-11-20 14:47:18 <br/>
 *
 * @author Wojciech Niemiec
 */
public interface ConnectionStateListener<T extends UserContext> {

  /**
   * Method invoked after connection with given user is established.
   *
   * @param userContext                  connected user.
   * @param connectionStateListenerChain connection state listener chain.
   */
  void userConnected(T userContext, ConnectionStateListenerChain connectionStateListenerChain);

  /**
   * Method invoked <strong>after</strong> connection with given user is closed.
   * In addition, user doesn't exists in users container and cannot receive any further messages.
   *
   * @param userContext                  disconnected user.
   * @param connectionStateListenerChain connection state listener chain.
   */
  void userDisconnected(T userContext, ConnectionStateListenerChain connectionStateListenerChain);
}
