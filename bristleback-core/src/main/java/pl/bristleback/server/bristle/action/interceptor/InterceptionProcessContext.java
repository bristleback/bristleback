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

package pl.bristleback.server.bristle.action.interceptor;

import pl.bristleback.server.bristle.action.ActionExecutionContext;

/**
 * This class is a container used in action interception process.
 * It contains interceptor context object and interceptor information.
 * Objects of this class are created for each interception process definition
 * (but not for each interception operation).
 * <p/>
 * Created on: 04.02.13 20:06 <br/>
 *
 * @author Wojciech Niemiec
 */
public class InterceptionProcessContext {

  private Object interceptorContext;

  private ActionInterceptorInformation interceptorInformation;

  public InterceptionProcessContext(Object interceptorContext, ActionInterceptorInformation interceptorInformation) {
    this.interceptorContext = interceptorContext;
    this.interceptorInformation = interceptorInformation;
  }

  public void intercept(ActionExecutionContext context) {
    interceptorInformation.getInterceptorInstance().intercept(context, interceptorContext);
  }

  public ActionInterceptorInformation getInterceptorInformation() {
    return interceptorInformation;
  }
}
