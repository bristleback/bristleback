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

package pl.bristleback.server.bristle.api.annotations;

import pl.bristleback.server.bristle.api.action.ActionInterceptor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Marks single action or whole action class as intercepted by the interceptors listed in annotation values.
 * Interceptors must be the valid Spring beans.
 * Both interceptor bean classes and interceptor bean names can be specified
 * (note that if bean class is provided and there is no bean of that type or more than one bean is found,
 * exception is thrown).
 * <p/>
 * Created on: 24.01.13 19:34 <br/>
 *
 * @author Wojciech Niemiec
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface Intercept {

  /**
   * Interceptor classes. If bean of any given type cannot be found or there is more than one bean defined,
   * exception is thrown.
   *
   * @return Interceptor classes.
   */
  Class<? extends ActionInterceptor>[] value() default {};

  /**
   * Spring bean names of the interceptors.
   *
   * @return Spring bean names of the interceptors.
   */
  String[] refs() default {};
}
