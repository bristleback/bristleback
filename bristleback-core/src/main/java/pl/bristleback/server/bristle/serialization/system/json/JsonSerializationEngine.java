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

package pl.bristleback.server.bristle.serialization.system.json;

import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.api.BristlebackConfig;
import pl.bristleback.server.bristle.api.SerializationEngine;
import pl.bristleback.server.bristle.api.SerializationResolver;
import pl.bristleback.server.bristle.serialization.system.PropertySerialization;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-11-11 15:40:11 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component("system.serializer.json")
public class JsonSerializationEngine implements SerializationEngine<PropertySerialization> {

  @Inject
  @Named("system.serializationResolver")
  private SerializationResolver<PropertySerialization> serializationResolver;

  @Inject
  private JsonFastSerializer fastSerializer;

  @Inject
  private JsonFastDeserializer fastDeserializer;

  @Override
  public void init(BristlebackConfig configuration) {
    serializationResolver.init(configuration);
  }

  @Override
  public Object deserialize(String serializedObject, PropertySerialization serialization) throws Exception {
    return fastDeserializer.deserialize(serializedObject, serialization);
  }

  @Override
  public String serialize(Object object, PropertySerialization serialization) throws Exception {
    return fastSerializer.serializeObject(object, serialization);
  }

  @Override
  public String serialize(Object object) throws Exception {
    PropertySerialization serialization = serializationResolver.resolveSerialization(object.getClass());
    return serialize(object, serialization);
  }

  @Override
  public SerializationResolver<PropertySerialization> getSerializationResolver() {
    return serializationResolver;
  }
}
