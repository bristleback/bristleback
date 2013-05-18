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

import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.action.ActionExecutionContext;
import pl.bristleback.server.bristle.action.ActionInformation;

import java.util.List;

/**
 * //@todo class description
 * <p/>
 * Created on: 20.01.13 11:58 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component
public class ActionInterceptorsExecutor {

  public void executeInterceptorPolicy(ActionInformation actionInformation, ActionExecutionContext context) {
    List<InterceptionProcessContext> interceptors = actionInformation.getActionInterceptors().getInterceptorsForStage(context.getStage());
    if (interceptors.isEmpty()) {
      return;
    }
    for (InterceptionProcessContext interceptionOperationContext : interceptors) {
      interceptionOperationContext.intercept(actionInformation, context);
    }
  }
}
