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
 * Classes marked with this annotation are considered as Bristleback Client Action Classes.
 * Client action classes are high level mechanism, allowing sending messages to client using Java methods.
 * To enable client actions, Bristleback intercepts client action classes methods and transparently sends messages to client.
 * <p/>
 * Created on: 2012-05-26 10:34:25 <br/>
 *
 * @author Wojciech Niemiec
 * @see pl.bristleback.server.bristle.api.annotations.ClientAction ClientAction
 */
@Target({ElementType.TYPE})
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface ClientActionClass {

  /**
   * Custom client action class name.
   * Names should follow normal Java classes naming conventions, e.g., should start with Capital letter,
   * contain only alphanumeric characters, etc. If custom name is not specified, client action class simple name
   * ({@link Class#getSimpleName()}) is used.
   *
   * @return custom action class name.
   */
  String name() default "";
}
