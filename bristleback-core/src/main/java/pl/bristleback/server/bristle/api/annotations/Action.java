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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * This annotation marks method as action method.
 * Both default and non default actions must be annotated to be processed by the action controller.
 * <p/>
 * Created on: 2011-10-02 14:26:59 <br/>
 *
 * @author Wojciech Niemiec
 */
@Target({ElementType.METHOD})
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface Action {

  /**
   * Name of this action. Names should follow normal methods naming conventions, e.g., should start with small letter,
   * contain only alphanumeric characters, etc.
   * If custom name is not specified, action method name is used.
   * There cannot be multiple action classes with the same name.
   *
   * @return action name.
   */
  String name() default "";
}
