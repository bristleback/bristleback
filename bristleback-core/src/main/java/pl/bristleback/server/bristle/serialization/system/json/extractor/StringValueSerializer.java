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
import pl.bristleback.server.bristle.serialization.system.json.converter.JsonTokenizer;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-09-04 18:23:44 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component
public class StringValueSerializer implements ValueSerializer<String> {

  @Override
  public void init(BristlebackConfig configuration) {
  }

  @Override
  public String toValue(JsonTokenizer tokenizer, PropertySerialization information) {
    return tokenizer.nextValueAsString();
  }

  @Override
  public String toText(String value, PropertySerialization information) {
    return JsonTokenizer.quote(value);
  }
}