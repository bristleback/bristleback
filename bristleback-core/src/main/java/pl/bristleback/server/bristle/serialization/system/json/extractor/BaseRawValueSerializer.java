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
import pl.bristleback.server.bristle.BristleRuntimeException;
import pl.bristleback.server.bristle.serialization.system.PropertySerialization;
import pl.bristleback.server.bristle.serialization.system.json.converter.JsonTokenType;
import pl.bristleback.server.bristle.serialization.system.json.converter.JsonTokenizer;

/**
 * This is a convenient class for value serializers that use simple json value (either stored as number, String or boolean).
 * <p/>
 * <p/>
 * Created on: 05.01.13 16:36 <br/>
 *
 * @author Wojciech Niemiec
 */
public abstract class BaseRawValueSerializer<T> implements ValueSerializer<T> {

  @Override
  public void init(BristlebackConfig configuration) {

  }

  @Override
  public T toValue(JsonTokenizer tokenizer, PropertySerialization information) throws Exception {
    JsonTokenType tokenType = tokenizer.nextToken();
    if (tokenType != JsonTokenType.PROPERTY_VALUE && tokenType != JsonTokenType.PROPERTY_NAME_OR_RAW_VALUE) {
      throw new BristleRuntimeException("Bad type of value, expected 'text' or 'number' type");
    }
    return toValueFromString(tokenizer.getLastTokenValue(), information);
  }

  /**
   * More convenient version of
   * {@link ValueSerializer#toValue}
   * method
   *
   * @param valueAsString
   * @param information
   * @return
   * @throws Exception
   */
  protected abstract T toValueFromString(String valueAsString, PropertySerialization information) throws Exception;
}
