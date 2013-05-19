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
 * This annotation can be placed before each action parameter to provide validation.
 * Currently only format, required and skipped flags are supported. Other validations will be provided in next versions.
 * <p/>
 * Created on: 2011-07-26 12:34:56 <br/>
 *
 * @author Wojciech Niemiec
 */
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface Bind {

  Property[] properties() default {};

  /**
   * This property should be used when field is of simple type.
   *
   * @return true if this simple action parameter is required, false otherwise.
   */
  boolean required() default false;

  /**
   * Indicates whether exceptions should return detailed information about which fields contains errors.
   * This flag is temporary disabled.
   *
   * @return true if detailed error messages should be sent for bound object.
   */
  boolean detailedErrors() default false;

  /**
   * Specifies format in which annotated objects will be serialized/deserialized. Note that there must be
   * {@link pl.bristleback.server.bristle.serialization.system.json.extractor.FormattingValueSerializer} bean registered
   * for the type that wants to be serialized/deserialized using specified format.
   *
   * @return format format in which objects of the specified type will be serialized/deserialized.
   */
  String format() default "";
}
