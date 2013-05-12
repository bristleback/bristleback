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

package pl.bristleback.server.bristle.conf.resolver.action;

import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.message.BristleMessage;
import pl.bristleback.server.bristle.serialization.system.annotation.Serialize;
import pl.bristleback.server.bristle.utils.PropertyUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-05-31 20:13:22 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component
public class BristleMessageSerializationUtils {

  private BristleMessage<String[]> serializedArrayMessage = new BristleMessage<String[]>();

  private BristleMessage<String> simpleMessage = new BristleMessage<String>();

  @Serialize(required = true)
  private Map<String, Object> simpleMap = new HashMap<String, Object>();

  public Type getSerializedArrayMessageType() {
    return PropertyUtils.getDeclaredFieldType(BristleMessageSerializationUtils.class, "serializedArrayMessage");
  }

  public Type getSimpleMessageType() {
    return PropertyUtils.getDeclaredFieldType(BristleMessageSerializationUtils.class, "simpleMessage");
  }


  public Type getSimpleMapType() {
    return PropertyUtils.getDeclaredFieldType(BristleMessageSerializationUtils.class, "simpleMap");
  }

  public Annotation[] getSimpleMapAnnotations() {
    return PropertyUtils.getDeclaredField(BristleMessageSerializationUtils.class, "simpleMap").getAnnotations();
  }
}
