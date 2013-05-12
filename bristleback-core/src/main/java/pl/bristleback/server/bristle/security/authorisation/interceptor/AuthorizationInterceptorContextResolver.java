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

package pl.bristleback.server.bristle.security.authorisation.interceptor;

import pl.bristleback.server.bristle.action.ActionInformation;
import pl.bristleback.server.bristle.api.action.ActionInterceptorContextResolver;
import pl.bristleback.server.bristle.api.annotations.Authorized;

import java.util.Arrays;
import java.util.List;

/**
 * This class helps {@link AuthorizationInterceptor} and resolves required rights for action or action class,
 * using {@link pl.bristleback.server.bristle.api.annotations.Authorized} annotation. Resolved objects are of {@link RequiredRights} type.
 * <p/>
 * Created on: 09.02.13 18:52 <br/>
 *
 * @author Wojciech Niemiec
 */
public class AuthorizationInterceptorContextResolver implements ActionInterceptorContextResolver<RequiredRights> {

  @Override
  public RequiredRights resolveInterceptorContext(ActionInformation actionInformation) {
    Authorized authorizedAnnotation;
    if (actionInformation.getMethod().isAnnotationPresent(Authorized.class)) {
      authorizedAnnotation = actionInformation.getMethod().getAnnotation(Authorized.class);
    } else {
      authorizedAnnotation = actionInformation.getActionClass().getType().getAnnotation(Authorized.class);
    }
    List<String> requiredRights = Arrays.asList(authorizedAnnotation.value());
    return new RequiredRights(requiredRights);
  }
}
