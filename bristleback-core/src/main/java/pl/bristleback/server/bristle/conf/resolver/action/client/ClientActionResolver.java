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
import pl.bristleback.server.bristle.action.client.ClientActionInformation;
import pl.bristleback.server.bristle.action.client.ClientActionParameterInformation;
import pl.bristleback.server.bristle.action.client.strategy.ClientActionResponseStrategies;
import pl.bristleback.server.bristle.api.SerializationEngine;
import pl.bristleback.server.bristle.api.SerializationResolver;
import pl.bristleback.server.bristle.api.action.ClientActionSender;
import pl.bristleback.server.bristle.conf.resolver.action.ActionResolvingUtils;

import javax.inject.Inject;
import javax.inject.Named;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-07-05 21:13:21 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component
public class ClientActionResolver {

  private static final boolean ACTION_NAME_SHOULD_BE_VALIDATED = true;

  @Inject
  @Named("serializationEngine")
  private SerializationEngine serializationEngine;

  @Inject
  private ClientActionParameterResolver parameterResolver;

  @Inject
  private ClientActionResponseStrategies responseStrategies;

  public ClientActionInformation prepareActionInformation(String actionClassName, Method actionMethod) {
    String actionName = ActionResolvingUtils.resolveClientActionName(actionMethod, ACTION_NAME_SHOULD_BE_VALIDATED);
    String fullName = ActionResolvingUtils.resolveFullName(actionName, actionClassName);

    List<ClientActionParameterInformation> parameters = resolveActionParameters(actionMethod);
    ClientActionSender actionSender = resolveActionResponse(actionMethod);
    return new ClientActionInformation(actionName, fullName, parameters, actionSender);
  }

  private ClientActionSender resolveActionResponse(Method actionMethod) {
    return responseStrategies.getStrategy(actionMethod.getReturnType());
  }

  private List<ClientActionParameterInformation> resolveActionParameters(Method actionMethod) {
    SerializationResolver serializationResolver = serializationEngine.getSerializationResolver();
    List<ClientActionParameterInformation> parameters = new ArrayList<ClientActionParameterInformation>();
    for (int i = 0; i < actionMethod.getParameterTypes().length; i++) {
      Type parameterType = actionMethod.getGenericParameterTypes()[i];
      ClientActionParameterInformation parameterInformation =
        parameterResolver.prepareActionParameter(serializationResolver, parameterType, actionMethod.getParameterAnnotations()[i]);
      parameters.add(parameterInformation);
    }
    return parameters;
  }
}
