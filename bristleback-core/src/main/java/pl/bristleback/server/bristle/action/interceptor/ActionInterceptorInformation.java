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

import pl.bristleback.server.bristle.action.ActionExecutionStage;
import pl.bristleback.server.bristle.api.action.ActionInterceptor;
import pl.bristleback.server.bristle.api.action.ActionInterceptorContextResolver;

/**
 * This class contains information about single action interceptor.
 * It contains interceptor instance, interception process context object resolver
 * and information about action stages to which this interceptor applies.
 * <p/>
 * Created on: 04.02.13 18:37 <br/>
 *
 * @author Wojciech Niemiec
 */
public class ActionInterceptorInformation {

  private ActionInterceptor interceptorInstance;

  private ActionInterceptorContextResolver interceptorContextResolver;

  private ActionExecutionStage[] stages;

  public ActionInterceptorInformation(ActionInterceptor interceptorInstance,
                                      ActionInterceptorContextResolver interceptorContextResolver,
                                      ActionExecutionStage[] stages) {
    this.interceptorInstance = interceptorInstance;
    this.interceptorContextResolver = interceptorContextResolver;
    this.stages = stages;
  }

  public ActionInterceptor getInterceptorInstance() {
    return interceptorInstance;
  }

  public ActionInterceptorContextResolver getInterceptorContextResolver() {
    return interceptorContextResolver;
  }

  public ActionExecutionStage[] getStages() {
    return stages;
  }
}
