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
 * Using implementations of this interface, user can easily organize action interceptors and assign them to actions
 * without {@link pl.bristleback.server.bristle.api.annotations.Intercept} annotations.
 * There are numerous built in interceptors matcher implementations.
 * To use interceptor matcher in your application,
 * create {@link pl.bristleback.server.bristle.action.interceptor.ActionInterceptorMapping} bean and
 * set {@link ActionInterceptor} and {@link ActionInterceptorMatcher} properties.
 * <p/>
 * Created on: 10.02.13 20:41 <br/>
 *
 * @author Wojciech Niemiec
 * @see pl.bristleback.server.bristle.action.interceptor.ActionInterceptorMapping ActionInterceptorMapping
 */
public interface ActionInterceptorMatcher {

  /**
   * Checks whether given action should be intercepted with {@link ActionInterceptor} implementation, mapped together with this matcher in
   * {@link pl.bristleback.server.bristle.action.interceptor.ActionInterceptorMapping} object.
   *
   * @param actionInformation action information
   * @return true if given action should be intercepted, false otherwise.
   */
  boolean isInterceptorApplicable(ActionInformation actionInformation, Class<? extends ActionInterceptor> interceptor);
}
