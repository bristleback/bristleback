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
import pl.bristleback.server.bristle.api.action.ClientActionSender;
import pl.bristleback.server.bristle.integration.spring.BristleSpringIntegration;
import pl.bristleback.server.bristle.utils.ReflectionUtils;

import javax.inject.Inject;
import javax.inject.Named;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-07-07 21:13:32 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component
public class ClientActionResponsesResolver {

  @Inject
  @Named("springIntegration")
  private BristleSpringIntegration springIntegration;

  public Map<Class, ClientActionSender> resolve() {
    Map<Class, ClientActionSender> strategies = new HashMap<Class, ClientActionSender>();

    Map<String, ClientActionSender> strategiesBeans = springIntegration.getBeansOfType(ClientActionSender.class);
    for (ClientActionSender strategy : strategiesBeans.values()) {
      Type type = ReflectionUtils.getParameterTypes(strategy.getClass(), ClientActionSender.class)[0];
      Class objectType;
      if (type instanceof ParameterizedType) {
        objectType = (Class) ((ParameterizedType) type).getRawType();
      } else {
        objectType = (Class) type;
      }

      strategies.put(objectType, strategy);
    }

    return strategies;
  }
}
