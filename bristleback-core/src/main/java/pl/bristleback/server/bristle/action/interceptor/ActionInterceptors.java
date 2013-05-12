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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * //@todo class description
 * <p/>
 * Created on: 20.01.13 12:04 <br/>
 *
 * @author Wojciech Niemiec
 */
public class ActionInterceptors {

  private Map<ActionExecutionStage, List<InterceptionProcessContext>> interceptors = new HashMap<ActionExecutionStage, List<InterceptionProcessContext>>();

  public ActionInterceptors(Map<ActionExecutionStage, List<InterceptionProcessContext>> interceptors) {
    this.interceptors = interceptors;
  }

  public List<InterceptionProcessContext> getInterceptorsForStage(ActionExecutionStage stage) {
    return interceptors.get(stage);
  }
}
