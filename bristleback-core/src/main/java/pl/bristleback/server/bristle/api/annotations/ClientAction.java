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
 * This annotation marks method as client action method. Together with {@link pl.bristleback.server.bristle.api.annotations.ClientActionClass ClientActionClass},
 * client action annotation provides complete information about single client action.
 * Using client action methods,
 * server can send messages to client. Method parameters are serialized to array as the message payload.
 * Under the hood, {@link pl.bristleback.server.bristle.message.ConditionObjectSender ConditionObjectSender} is used.
 * Each parameter can be validated using {@link pl.bristleback.server.bristle.serialization.system.annotation.Bind Bind} annotation.
 * If any parameter is not meant to be serialized, simply use {@link pl.bristleback.server.bristle.api.annotations.Ignore} on that parameter.
 * Action methods must have one of the following return types:
 * <ul>
 * <li>
 * {@link pl.bristleback.server.bristle.api.action.SendCondition SendCondition} implementation.
 * </li>
 * <li>
 * {@link pl.bristleback.server.bristle.api.users.UserContext UserContext} implementation.
 * </li>
 * <li>
 * List of {@link pl.bristleback.server.bristle.api.users.UserContext UserContext} implementations.
 * </li>
 * </ul>
 * List of recipients is determined using returned object.
 * <p/>
 * Created on: 2012-05-26 10:38:26 <br/>
 *
 * @author Wojciech Niemiec
 * @see pl.bristleback.server.bristle.api.annotations.ClientActionClass ClientActionClass
 */
@Target({ElementType.METHOD})
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface ClientAction {

  /**
   * Gets custom client action name. If not defined, method name is used as client action name.
   *
   * @return custom client action name.
   */
  String value() default "";
}
