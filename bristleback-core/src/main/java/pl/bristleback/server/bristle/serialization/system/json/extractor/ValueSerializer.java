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

import pl.bristleback.server.bristle.api.BristlebackConfig;
import pl.bristleback.server.bristle.serialization.system.PropertySerialization;
import pl.bristleback.server.bristle.serialization.system.json.converter.JsonTokenizer;

/**
 * Implementations of this interface should be capable of converting data stored as json into some specified Java objects
 * and from Java objects into json form. Standard Bristleback serialization engine contains several built in implementations
 * for the most fundamental Java types (numbers, String variables, dates, boolean variables, enums, etc).
 * <p/>
 * Created on: 2011-07-26 13:22:02 <br/>
 *
 * @author Wojciech Niemiec
 * @see FormattingValueSerializer
 */
public interface ValueSerializer<T> {

  /**
   * This method can be implemented if Bristleback configuration object is somehow used by the value serializer implementation.
   *
   * @param configuration Bristleback configuration.
   */
  void init(BristlebackConfig configuration);

  /**
   * Converts json value stored as text into Java object.
   *
   * @param tokenizer   tokenizer is used to retrieve simple or complex json tokens from text.
   * @param information serialization information, containing constraints, format information,
   *                    children properties access objects, etc.
   * @return deserialized Java object.
   * @throws Exception if any exception occurs during the deserialization process.
   */
  T toValue(JsonTokenizer tokenizer, PropertySerialization information) throws Exception;

  /**
   * Serializes Java object as a Json text.
   *
   * @param value       value to serialize.
   * @param information serialization information, containing constraints, format information,
   *                    children properties access objects, etc.
   * @return value stored as a Json text value.
   * @throws Exception
   */
  String toText(T value, PropertySerialization information) throws Exception;
}
