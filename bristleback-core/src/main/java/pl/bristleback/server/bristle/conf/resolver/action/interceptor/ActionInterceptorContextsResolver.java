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

package pl.bristleback.server.bristle.conf.resolver.action.interceptor;

import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.action.ActionInformation;
import pl.bristleback.server.bristle.action.interceptor.ActionInterceptorInformation;
import pl.bristleback.server.bristle.action.interceptor.InterceptionProcessContext;
import pl.bristleback.server.bristle.api.action.ActionInterceptor;
import pl.bristleback.server.bristle.conf.BristleInitializationException;
import pl.bristleback.server.bristle.utils.ReflectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to resolve {@link InterceptionProcessContext} list,
 * where each context object is responsible for single interception operation.
 * <p/>
 * Created on: 08.02.13 17:40 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component
public class ActionInterceptorContextsResolver {

  public List<InterceptionProcessContext> resolveContexts(List<ActionInterceptorInformation> informationList, ActionInformation actionInformation) {
    List<InterceptionProcessContext> contextList = new ArrayList<InterceptionProcessContext>();
    for (ActionInterceptorInformation interceptorInformation : informationList) {
      Object contextObject = getContextObjectUsingActionInformation(interceptorInformation, actionInformation);
      contextList.add(new InterceptionProcessContext(contextObject, interceptorInformation));
    }

    return contextList;
  }

  private Object getContextObjectUsingActionInformation(ActionInterceptorInformation interceptorInformation, ActionInformation actionInformation) {
    Object contextObject = interceptorInformation.getInterceptorContextResolver().resolveInterceptorContext(actionInformation);
    checkContextObjectCompatibility(contextObject, interceptorInformation);
    return contextObject;
  }

  private void checkContextObjectCompatibility(Object contextObject, ActionInterceptorInformation interceptorInformation) {
    Class<?> interceptorInstanceType = interceptorInformation.getInterceptorInstance().getClass();
    Class<?> interceptorContextObjectType = (Class) ReflectionUtils
      .getParameterTypes(interceptorInstanceType, ActionInterceptor.class)[0];

    if (!interceptorContextObjectType.isAssignableFrom(contextObject.getClass())) {
      throw new BristleInitializationException("Object returned by "
        + interceptorInformation.getInterceptorContextResolver().getClass().getName()
        + " context resolver does not match context parameter type in " + interceptorInstanceType.getName()
        + " interceptor class.");
    }
  }
}
