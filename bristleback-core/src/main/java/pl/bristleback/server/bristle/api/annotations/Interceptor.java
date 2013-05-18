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

import pl.bristleback.server.bristle.action.ActionExecutionStage;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Beans marked with this annotation must implement {@link pl.bristleback.server.bristle.api.action.ActionInterceptor} interface.
 * This annotation defines action execution stages, to which marked interceptor applies
 * and the class that prepares interception execution context.
 * Interceptor will be invoked <strong>AFTER</strong> execution stages listed in <code>stages</code> annotation property.
 * For an execution stage it is possible to control order of interceptors invocations by using the spring annotation
 * {@link org.springframework.core.annotation.Order}
 * Following stages can be intercepted:
 * <ul>
 * <li>After {@link ActionExecutionStage#ACTION_EXTRACTION}</li>
 * <li>After {@link ActionExecutionStage#PARAMETERS_EXTRACTION}</li>
 * <li>After {@link ActionExecutionStage#ACTION_EXECUTION}</li>
 * </ul>
 * <p/>
 * Created on: 24.01.13 20:16 <br/>
 *
 * @author Wojciech Niemiec
 * @see ActionExecutionStage
 */
@Target({ElementType.TYPE})
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface Interceptor {

  ActionExecutionStage[] stages();
}
