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

import pl.bristleback.server.bristle.serialization.system.PropertySerialization;
import pl.bristleback.server.bristle.serialization.system.PropertySerializationConstraints;
import pl.bristleback.server.bristle.serialization.system.json.converter.JsonTokenizer;

import java.text.NumberFormat;
import java.text.ParseException;

/**
 * Base class for built in number formatting serializers, containing utility methods for serializers internal usage.
 * <p/>
 * Created on: 22.12.12 13:44 <br/>
 *
 * @author Wojciech Niemiec
 */
public abstract class BaseNumberFormattingValueSerializer<T extends Number> extends BaseRawValueSerializer<T> implements FormattingValueSerializer<T> {

  protected abstract NumberFormat createNumberFormatObject(String formatAsString);

  protected abstract T parseFromNotFormattedText(String valueAsString, PropertySerialization information);

  @Override
  public Object prepareFormatHolder(final String formatAsString) {
    return new ThreadLocal<NumberFormat>() {
      @Override
      protected NumberFormat initialValue() {
        return createNumberFormatObject(formatAsString);
      }
    };
  }

  @Override
  protected T toValueFromString(String valueAsString, PropertySerialization information) throws ParseException {
    if (information.getConstraints().isFormatted()) {
      return parseFromFormattedString(valueAsString, information);
    }
    return parseFromNotFormattedText(valueAsString, information);
  }

  @SuppressWarnings("unchecked")
  protected T parseFromFormattedString(String valueAsString, PropertySerialization information) throws ParseException {
    return (T) getFormat(information.getConstraints()).parse(valueAsString);
  }

  @Override
  public String toText(T value, PropertySerialization information) {
    if (information.getConstraints().isFormatted()) {
      return JsonTokenizer.quote(getFormat(information.getConstraints()).format(value));
    }
    return value.toString();
  }

  @SuppressWarnings("unchecked")
  protected NumberFormat getFormat(PropertySerializationConstraints constraints) {
    return ((ThreadLocal<NumberFormat>) constraints.getFormatHolder()).get();
  }
}


