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

package pl.bristleback.server.bristle.action.interceptor.matcher;

import pl.bristleback.server.bristle.action.ActionInformation;
import pl.bristleback.server.bristle.api.action.ActionInterceptor;
import pl.bristleback.server.bristle.api.action.ActionInterceptorMatcher;

import java.util.List;

/**
 * Logical operations used in {@link CompositeInterceptorMatcher} class.
 * <p/>
 * Created on: 14.02.13 19:25 <br/>
 *
 * @author Wojciech Niemiec
 */
public enum LogicalOperation {

  AND {
    @Override
    public boolean operateWith(List<ActionInterceptorMatcher> matcherList, ActionInformation actionInformation,
                               Class<? extends ActionInterceptor> interceptor) {
      for (ActionInterceptorMatcher actionInterceptorMatcher : matcherList) {
        boolean result = actionInterceptorMatcher.isInterceptorApplicable(actionInformation, interceptor);
        if (!result) {
          return false;
        }
      }
      return true;
    }
  },
  OR {
    @Override
    public boolean operateWith(List<ActionInterceptorMatcher> matcherList, ActionInformation actionInformation,
                               Class<? extends ActionInterceptor> interceptor) {
      for (ActionInterceptorMatcher actionInterceptorMatcher : matcherList) {
        boolean result = actionInterceptorMatcher.isInterceptorApplicable(actionInformation, interceptor);
        if (result) {
          return true;
        }
      }
      return false;
    }
  },
  NOT {
    @Override
    public boolean operateWith(List<ActionInterceptorMatcher> matcherList, ActionInformation actionInformation,
                               Class<? extends ActionInterceptor> interceptor) {
      return !AND.operateWith(matcherList, actionInformation, interceptor);
    }
  };

  public abstract boolean operateWith(List<ActionInterceptorMatcher> matcherList,
                                      ActionInformation actionInformation, Class<? extends ActionInterceptor> interceptor);
}
