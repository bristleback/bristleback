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
 * This annotation allows to define non default serialization operations, for example, by providing validation.
 * It can be used both with {@link pl.bristleback.server.bristle.message.ConditionObjectSender} definitions and
 * above server action methods.
 * It is possible to define serialization of generic containers (maps, collections and arrays) by specifying {@link Serialize#containerElementClass()}.
 * Unfortunately, containers of parametrized types cannot be defined.
 * <p/>
 * Created on: 2011-09-04 16:40:46 <br/>
 *
 * @author Wojciech Niemiec
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface Serialize {

  /**
   * Type of object covered by this serialization definition.
   *
   * @return type of object covered by this serialization definition.
   */
  Class target() default Object.class;

  /**
   * Because of type erasure, specifying container element type cannot be done using {@link Serialize#target()} method.
   * This attribute can be helpful in such situations.
   *
   * @return container element class.
   */
  Class containerElementClass() default Object.class;

  /**
   * Serialization definition of nested properties.
   *
   * @return serialization definition of nested properties.
   */
  Property[] properties() default {};

  /**
   * This property should be used when serialized object is of simple type.
   *
   * @return true if this simple action parameter is required, false otherwise.
   */
  boolean required() default false;

  /**
   * Specifies format in which objects of type specified in {@link pl.bristleback.server.bristle.serialization.system.annotation.Serialize#target()} property
   * will be serialized. Note that there must be
   * {@link pl.bristleback.server.bristle.serialization.system.json.extractor.FormattingValueSerializer} bean registered
   * for type that wants to be serialized using specified format.
   *
   * @return format format in which objects of the specified type will be serialized.
   */
  String format() default "";
}
