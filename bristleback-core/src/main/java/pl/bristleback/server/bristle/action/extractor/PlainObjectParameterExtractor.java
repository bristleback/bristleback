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

package pl.bristleback.server.bristle.action.extractor;

import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.action.ActionExecutionContext;
import pl.bristleback.server.bristle.action.ActionParameterInformation;
import pl.bristleback.server.bristle.api.BristlebackConfig;
import pl.bristleback.server.bristle.api.SerializationEngine;
import pl.bristleback.server.bristle.api.action.ActionParameterExtractor;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-07-20 09:29:08 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component
public class PlainObjectParameterExtractor implements ActionParameterExtractor {

  @Inject
  @Named("serializationEngine")
  private SerializationEngine serializationEngine;

  @Override
  @SuppressWarnings("unchecked")
  public Object fromTextContent(String text, ActionParameterInformation parameterInformation, ActionExecutionContext context) throws Exception {
    return serializationEngine.deserialize(text, parameterInformation.getPropertySerialization());
  }

  @Override
  public boolean isDeserializationRequired() {
    return true;
  }

  public void init(BristlebackConfig configuration) {
  }
}
