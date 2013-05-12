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

package pl.bristleback.server.bristle.serialization.system.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * If there is more than one serialization to be specified in {@link pl.bristleback.server.bristle.message.ConditionObjectSender} object,
 * this container annotation gathers multiple {@link Serialize} annotations.
 * Note that there can be maximum one serialization per payload type.
 * <p/>
 * <p/>
 * Created on: 15.12.12 21:53 <br/>
 *
 * @author Wojciech Niemiec
 */
@Target({ElementType.FIELD})
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface SerializeBundle {

  Serialize[] value();
}
