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

import java.util.Map;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-07-10 22:32:12 <br/>
 *
 * @author Wojciech Niemiec
 */
public class EnumSerializer extends BaseRawValueSerializer<Enum> {

  private Map<String, Enum> enumValues;

  public EnumSerializer(Map<String, Enum> enumValues) {
    this.enumValues = enumValues;
  }

  @Override
  public void init(BristlebackConfig configuration) {

  }

  @Override
  protected Enum toValueFromString(String valueAsString, PropertySerialization information) throws Exception {
    return enumValues.get(valueAsString);
  }

  @Override
  public String toText(Enum value, PropertySerialization information) throws Exception {
    return JsonTokenizer.quote(value.name());
  }
}
