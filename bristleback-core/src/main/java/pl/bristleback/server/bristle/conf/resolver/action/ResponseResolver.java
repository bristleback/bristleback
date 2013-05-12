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
import pl.bristleback.server.bristle.action.response.ActionResponseInformation;
import pl.bristleback.server.bristle.api.SerializationEngine;
import pl.bristleback.server.bristle.api.SerializationResolver;

import javax.inject.Inject;
import javax.inject.Named;
import java.lang.reflect.Method;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-06-16 12:13:12 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component
public class ResponseResolver {

  @Inject
  @Named("serializationEngine")
  private SerializationEngine serializationEngine;

  @Inject
  private BristleMessageSerializationUtils messageSerializationUtils;

  ActionResponseInformation resolveResponse(Method action) {
    ActionResponseInformation responseInformation = new ActionResponseInformation();

    Class<?> actionReturnType = action.getReturnType();
    if (actionReturnType.equals(Void.class) || actionReturnType.equals(Void.TYPE)) {
      responseInformation.setVoidResponse(true);
    }

    resolveResponseSerialization(action, responseInformation);

    return responseInformation;
  }

  @SuppressWarnings("unchecked")
  private void resolveResponseSerialization(Method action, ActionResponseInformation responseInformation) {

    SerializationResolver serializationResolver = serializationEngine.getSerializationResolver();
    Object serialization = serializationResolver.resolveSerialization(messageSerializationUtils.getSimpleMessageType());

    Object payloadSerialization = serializationResolver.resolveSerialization(action.getGenericReturnType(), action.getAnnotations());
    serializationResolver.setSerializationForField(serialization, "payload", payloadSerialization);

    responseInformation.setSerialization(serialization);
  }
}
