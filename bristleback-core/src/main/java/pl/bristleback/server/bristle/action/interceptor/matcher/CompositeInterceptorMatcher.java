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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This is a convenient logical interceptor matcher, for general usage.
 * It allows gather two or more interceptor matcher objects using several logical operations.
 * Logical operations are defined in {@link LogicalOperation} class.
 * <p/>
 * Created on: 14.02.13 18:34 <br/>
 *
 * @author Wojciech Niemiec
 * @see LogicalOperation
 */
public class CompositeInterceptorMatcher implements ActionInterceptorMatcher {

  private LogicalOperation operation = LogicalOperation.AND;

  private List<ActionInterceptorMatcher> matcherList = new ArrayList<ActionInterceptorMatcher>();

  public CompositeInterceptorMatcher() {
  }

  public CompositeInterceptorMatcher(ActionInterceptorMatcher... matcherArray) {
    this.matcherList = Arrays.asList(matcherArray);
  }

  public CompositeInterceptorMatcher(List<ActionInterceptorMatcher> matcherList) {
    this.matcherList = matcherList;
  }

  public CompositeInterceptorMatcher(LogicalOperation operation, List<ActionInterceptorMatcher> matcherList) {
    this.operation = operation;
    this.matcherList = matcherList;
  }

  public CompositeInterceptorMatcher(LogicalOperation operation, ActionInterceptorMatcher... matcherArray) {
    this.operation = operation;
    this.matcherList = Arrays.asList(matcherArray);
  }

  @Override
  public boolean isInterceptorApplicable(ActionInformation actionInformation, Class<? extends ActionInterceptor> interceptor) {
    return operation.operateWith(matcherList, actionInformation, interceptor);
  }

  public void setMatcherArray(ActionInterceptorMatcher... matcherArray) {
    matcherList = Arrays.asList(matcherArray);
  }

  public void setOperation(LogicalOperation operation) {
    this.operation = operation;
  }

  public void setMatcherList(List<ActionInterceptorMatcher> matcherList) {
    this.matcherList = matcherList;
  }
}
