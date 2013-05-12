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

package pl.bristleback.server.bristle.serialization;

import java.util.HashMap;
import java.util.Map;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-10-08 14:46:06 <br/>
 *
 * @author Wojciech Niemiec
 */
public class SerializationBundle {

  private Map<Class, Object> serializations = new HashMap<Class, Object>();

  public Object getSerialization(Class payloadType) {
    return serializations.get(payloadType);
  }

  public void addSerialization(Class payloadType, Object serialization) {
    if (isSerializationForPayloadTypeExist(payloadType)) {
      throw new SerializationResolvingException("Default serialization for type " + payloadType + " already exists");
    }
    serializations.put(payloadType, serialization);
  }

  public boolean isSerializationForPayloadTypeExist(Class payloadType) {
    return serializations.containsKey(payloadType);
  }
}