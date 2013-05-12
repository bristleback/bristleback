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

import pl.bristleback.server.bristle.api.action.ActionInterceptor;
import pl.bristleback.server.bristle.api.action.ActionInterceptorMatcher;

/**
 * Action interceptor mapping beans are defined as the supplement for
 * {@link pl.bristleback.server.bristle.api.annotations.Intercept} annotations.
 * Mappings consists of interceptor bean/class definition and {@link ActionInterceptorMatcher} bean,
 * which is responsible for checking all potential action classes and deciding whether given action should be intercepted
 * with interceptor from mapping.
 * <p/>
 * Created on: 12.02.13 18:55 <br/>
 *
 * @author Wojciech Niemiec
 */
public class ActionInterceptorMapping {

  private Class<? extends ActionInterceptor> interceptorClass;

  private ActionInterceptor interceptor;

  private ActionInterceptorMatcher interceptorMatcher;

  public ActionInterceptorMapping() {
  }

  public ActionInterceptorMapping(Class<? extends ActionInterceptor> interceptorClass, ActionInterceptorMatcher interceptorMatcher) {
    this.interceptorClass = interceptorClass;
    this.interceptorMatcher = interceptorMatcher;
  }

  public ActionInterceptorMapping(ActionInterceptor interceptor, ActionInterceptorMatcher interceptorMatcher) {
    this.interceptor = interceptor;
    this.interceptorMatcher = interceptorMatcher;
  }

  public Class<? extends ActionInterceptor> getInterceptorClass() {
    if (isInterceptorBeanSpecified()) {
      return interceptor.getClass();
    }
    return interceptorClass;
  }

  public void setInterceptorClass(Class<? extends ActionInterceptor> interceptorClass) {
    this.interceptorClass = interceptorClass;
  }

  public boolean isInterceptorBeanSpecified() {
    return interceptor != null;
  }

  public ActionInterceptorMatcher getInterceptorMatcher() {
    return interceptorMatcher;
  }

  public void setInterceptorMatcher(ActionInterceptorMatcher interceptorMatcher) {
    this.interceptorMatcher = interceptorMatcher;
  }

  public ActionInterceptor getInterceptor() {
    return interceptor;
  }

  public void setInterceptor(ActionInterceptor interceptor) {
    this.interceptor = interceptor;
  }
}
