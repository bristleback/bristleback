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

package pl.bristleback.server.bristle.conf.resolver.action.client;

import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.action.client.ClientActionParameterInformation;
import pl.bristleback.server.bristle.api.SerializationResolver;
import pl.bristleback.server.bristle.api.annotations.Ignore;
import pl.bristleback.server.bristle.utils.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * This component resolves meta information about client action parameter, encapsulated in {@link ClientActionParameterInformation} object.
 * <p/>
 * Created on: 2012-07-21 10:08:07 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component
public class ClientActionParameterResolver {

  public ClientActionParameterInformation prepareActionParameter(SerializationResolver serializationResolver,
                                                                 Type parameterType, Annotation[] annotations) {
    Ignore ignoreSerialization = ReflectionUtils.findAnnotation(annotations, Ignore.class);
    boolean forSerialization = ignoreSerialization == null;
    if (forSerialization) {
      Object serialization = serializationResolver.resolveSerialization(parameterType, annotations);
      return new ClientActionParameterInformation(parameterType, serialization);
    }
    return new ClientActionParameterInformation(parameterType);
  }
}
