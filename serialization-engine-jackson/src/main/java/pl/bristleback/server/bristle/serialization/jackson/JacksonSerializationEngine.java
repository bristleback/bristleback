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
import pl.bristleback.server.bristle.serialization.jackson.init.ObjectMapperInitializer;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * This serialization engine uses Jackson processing library.
 * {@link ObjectMapper} is used in serialization/deserialization operations.
 * To customize object mapper, create bean implementing
 * {@link pl.bristleback.server.bristle.serialization.jackson.init.ObjectMapperFactory} interface.
 * This is a default {@link SerializationEngine} implementation used in Bristleback Framework.
 * <p>
 * Name of this serialization engine: <strong>system.serializer.jackson</strong>
 * </p>
 * Jackson serialization module relies on following Jackson dependencies: jackson-core, jackson-databind and jackson-annotations,
 * all of them in version: 2.2.2. If you want to use other versions of Jackson,
 * you can exclude those three dependencies from your project and place them by yourself in versions you desire.
 * <p/>
 * Created on: 2012-03-09 18:27:48 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component("system.serializer.jackson")
public class JacksonSerializationEngine implements SerializationEngine<JacksonSerialization> {

  @Inject
  private ObjectMapperInitializer objectMapperInitializer;

  @Inject
  @Named("jacksonSerializer.serializationResolver")
  private JacksonSerializationResolver serializationResolver;

  private ObjectMapper mapper;

  @Override
  public void init(BristlebackConfig configuration) {
    mapper = objectMapperInitializer.initObjectMapper();
    serializationResolver.setObjectMapper(mapper);
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
}
