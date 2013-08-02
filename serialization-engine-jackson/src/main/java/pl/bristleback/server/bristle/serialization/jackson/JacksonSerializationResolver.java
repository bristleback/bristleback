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

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.api.SerializationResolver;
import pl.bristleback.server.bristle.serialization.SerializationBundle;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;

/**
 * Resolves serialization information using Jackson framework.
 * {@link ObjectMapper#constructType(java.lang.reflect.Type)} method is used to resolve serialization information.
 * <p/>
 * Created on: 2012-03-09 18:45:49 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component("jacksonSerializer.serializationResolver")
public class JacksonSerializationResolver implements SerializationResolver<JacksonSerialization> {

  private ObjectMapper objectMapper;

  void setObjectMapper(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public SerializationBundle initSerializationBundle(Field objectSenderField) {
    return new SerializationBundle();
  }

  @Override
  public JacksonSerialization resolveSerialization(Type objectType, Annotation... annotations) {
    JacksonSerialization serialization = new JacksonSerialization();
    JavaType rootType = objectMapper.constructType(objectType);

    serialization.setType(rootType);
    return serialization;
  }
}
