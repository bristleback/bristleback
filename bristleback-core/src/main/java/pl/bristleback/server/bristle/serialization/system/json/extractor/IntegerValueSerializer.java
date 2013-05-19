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

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-07-31 14:22:35 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component
public class IntegerValueSerializer extends BaseNumberFormattingValueSerializer<Integer> {

  @Override
  public void init(BristlebackConfig configuration) {

  }

  protected Integer parseFromFormattedString(String valueAsString, PropertySerialization information) throws ParseException {
    return getFormat(information.getConstraints()).parse(valueAsString).intValue();
  }

  @Override
  protected NumberFormat createNumberFormatObject(String formatAsString) {
    DecimalFormat format = new DecimalFormat(formatAsString);
    format.setParseBigDecimal(false);
    format.setParseIntegerOnly(true);
    return format;
  }

  @Override
  protected Integer parseFromNotFormattedText(String valueAsString, PropertySerialization information) {
    return Integer.parseInt(valueAsString);
  }
}