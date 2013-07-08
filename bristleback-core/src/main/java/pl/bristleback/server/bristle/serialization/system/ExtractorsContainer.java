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

package pl.bristleback.server.bristle.serialization.system;

import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.serialization.SerializationResolvingException;
import pl.bristleback.common.serialization.message.BristleMessage;
import pl.bristleback.server.bristle.serialization.system.json.extractor.ValueSerializer;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-07-26 13:23:29 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component
public class ExtractorsContainer {

  private Map<Class, ValueSerializer> enumProcessors;
  private Map<Class, ValueSerializer> valueProcessors;
  private Map<Type, PropertySerialization> defaultPropertySerializations;


  public ExtractorsContainer() {
    defaultPropertySerializations = new HashMap<Type, PropertySerialization>();
    enumProcessors = new HashMap<Class, ValueSerializer>();
  }

  public boolean containsPropertySerialization(Type objectType) {
    return defaultPropertySerializations.containsKey(objectType);
  }

  public boolean containsEnumSerializer(Class<? extends Enum> enumType) {
    return enumProcessors.containsKey(enumType);
  }

  public PropertySerialization getDefaultPropertySerialization(Type objectType) {
    return defaultPropertySerializations.get(objectType);
  }

  public void addDefaultPropertySerialization(PropertySerialization propertySerialization) {
    if (propertySerialization.getPropertyClass().equals(BristleMessage.class)) {
      return;
    }
    if (propertySerialization.getGenericType() != null) {
      defaultPropertySerializations.put(propertySerialization.getGenericType(), propertySerialization);
    } else {
      defaultPropertySerializations.put(propertySerialization.getPropertyClass(), propertySerialization);
    }
  }

  public void addEnumSerialization(Class<? extends Enum> enumType, ValueSerializer enumSerializer) {
    enumProcessors.put(enumType, enumSerializer);
  }

  public boolean containsValueProcessor(Class valueClass) {
    return valueProcessors.containsKey(valueClass);
  }

  public ValueSerializer getValueProcessor(Class objectClass) {
    return valueProcessors.get(objectClass);
  }

  public ValueSerializer getEnumSerializer(Class<? extends Enum> enumClass) {
    return enumProcessors.get(enumClass);
  }

  public <T> ValueSerializer getValueProcessorWithClass(Class<? extends ValueSerializer> processorClass) {
    for (ValueSerializer valueSerializer : valueProcessors.values()) {
      if (valueSerializer.getClass().equals(processorClass)) {
        return valueSerializer;
      }
    }
    throw new SerializationResolvingException("Cannot resolve value processor " + processorClass.getName());
  }

  public void setValueProcessors(Map<Class, ValueSerializer> valueProcessors) {
    this.valueProcessors = valueProcessors;
  }

}