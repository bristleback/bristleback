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

package pl.bristleback.server.bristle.api.action;

import pl.bristleback.server.bristle.action.ActionInformation;

/**
 * This helper interface serves as a interceptor context object provider for each interception operation type.
 * Implementations must be parametrized with the same type as interceptors they are serving.
 * <p/>
 * Created on: 04.02.13 19:52 <br/>
 *
 * @author Wojciech Niemiec
 */
public interface ActionInterceptorContextResolver<T> {

  /**
   * Prepares interception context object for given action.
   * Created object is passed as a parameter to
   * {@link ActionInterceptor} interface implementation served by this context resolver.
   *
   * @param actionInformation information about intercepted action.
   * @return interception operation context object.
   */
  T resolveInterceptorContext(ActionInformation actionInformation);
}
