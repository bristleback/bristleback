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

package pl.bristleback.server.bristle.serialization.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.api.BristlebackConfig;
import pl.bristleback.server.bristle.api.SerializationEngine;
import pl.bristleback.server.bristle.api.SerializationResolver;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-03-09 18:27:48 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component("system.jacksonSerializer")
public class JacksonSerializationEngine implements SerializationEngine<JacksonSerialization> {

  @Inject
  @Named("jacksonSerializer.serializationResolver")
  private SerializationResolver<JacksonSerialization> serializationResolver;

  private ObjectMapper mapper;

  @Override
  public void init(BristlebackConfig configuration) {
    mapper = new ObjectMapper();
  }

  @Override
  public SerializationResolver<JacksonSerialization> getSerializationResolver() {
    return serializationResolver;
  }

  @Override
  public Object deserialize(String serializedObject, JacksonSerialization serialization) throws Exception {
    return mapper.readValue(serializedObject, serialization.getType());
  }

  @Override
  public String serialize(Object object, JacksonSerialization serialization) throws Exception {
    return mapper.writeValueAsString(object);
  }

  @Override
  public String serialize(Object object) throws Exception {
    return mapper.writeValueAsString(object);
  }

  public void setSerializationResolver(SerializationResolver<JacksonSerialization> serializationResolver) {
    this.serializationResolver = serializationResolver;
  }

  public void setMapper(ObjectMapper mapper) {
    this.mapper = mapper;
  }
}
