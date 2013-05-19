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

import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.api.BristlebackConfig;
import pl.bristleback.server.bristle.serialization.system.PropertySerialization;
import pl.bristleback.server.bristle.serialization.system.PropertySerializationConstraints;
import pl.bristleback.server.bristle.serialization.system.json.converter.JsonTokenizer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-11-24 17:15:39 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component
public class DateValueSerializer extends BaseRawValueSerializer<Date> implements FormattingValueSerializer<Date> {

  @Override
  public void init(BristlebackConfig configuration) {
  }

  @Override
  public ThreadLocal<DateFormat> prepareFormatHolder(final String formatAsString) {
    return new ThreadLocal<DateFormat>() {
      @Override
      protected DateFormat initialValue() {
        return new SimpleDateFormat(formatAsString);
      }
    };
  }

  @Override
  protected Date toValueFromString(String valueAsString, PropertySerialization information) throws Exception {
    if (information.getConstraints().isFormatted()) {
      return getFormat(information.getConstraints()).parse(valueAsString);
    }
    return new Date(Long.parseLong(valueAsString));
  }

  @Override
  public String toText(Date value, PropertySerialization information) {
    if (information.getConstraints().isFormatted()) {
      return JsonTokenizer.quote(getFormat(information.getConstraints()).format(value));
    }
    return value.getTime() + "";
  }

  @SuppressWarnings("unchecked")
  private DateFormat getFormat(PropertySerializationConstraints constraints) {
    return ((ThreadLocal<DateFormat>) constraints.getFormatHolder()).get();
  }
}
