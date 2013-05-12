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

package pl.bristleback.server.bristle.serialization.system.json.extractor;

/**
 * Extended {@link ValueSerializer} interface, implementations should be able to handle <code>format</code> property
 * of the {@link pl.bristleback.server.bristle.serialization.system.annotation.Bind},
 * {@link pl.bristleback.server.bristle.serialization.system.annotation.Serialize} and
 * {@link pl.bristleback.server.bristle.serialization.system.annotation.Property} annotations.
 * Because it may be possible to have multiple threads using the same formatting objects,
 * returned format representation must be thread safe.
 * <p/>
 * Created on: 16.12.12 14:40 <br/>
 *
 * @author Wojciech Niemiec
 */
public interface FormattingValueSerializer<T> extends ValueSerializer<T> {

  /**
   * Creates any kind of object that will store information about format used in given field.
   * Returned format object MUST BE capable of performing formatting operations in multiple threads simultaneously.
   *
   * @param formatAsString format passed in serialization annotations.
   * @return format representation or any object providing information about current format.
   */
  Object prepareFormatHolder(String formatAsString);
}
