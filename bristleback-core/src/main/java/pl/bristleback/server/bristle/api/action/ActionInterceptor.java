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

import pl.bristleback.server.bristle.action.ActionExecutionContext;
import pl.bristleback.server.bristle.action.ActionInformation;

/**
 * Every action can be intercepted by any number of action interceptors. To be considered as an interceptor,
 * beans must be annotated with {@link pl.bristleback.server.bristle.api.annotations.Interceptor}.
 * Interception context object is resolved by the {@link ActionInterceptorContextResolver},
 * defined in {@link pl.bristleback.server.bristle.api.annotations.Intercept} annotation.
 * Additionally, each interceptor can be annotated by the spring annotation {@link org.springframework.core.annotation.Order}
 * to control order of their invocations.
 * <p/>
 * Created on: 20.01.13 11:52 <br/>
 *
 * @author Wojciech Niemiec
 */
public interface ActionInterceptor<T> {

  /**
   * Intercepts action execution. The latter execution stage is, the more information is available in {@link ActionExecutionContext} object.
   * Interceptor may modify or change various action execution information, as well as cancel response sending by calling
   * {@link pl.bristleback.server.bristle.action.ActionExecutionContext#cancelResponseSending()} method.
   *
   * @param actionInformation  meta information about executed action.
   * @param context            action execution context object, created for each action execution.
   * @param interceptorContext interception execution context, created for each interception operation.
   */
  void intercept(ActionInformation actionInformation, ActionExecutionContext context, T interceptorContext);
}
