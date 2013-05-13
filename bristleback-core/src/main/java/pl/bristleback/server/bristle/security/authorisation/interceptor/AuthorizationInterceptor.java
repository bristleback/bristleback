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

import pl.bristleback.server.bristle.action.ActionExecutionContext;
import pl.bristleback.server.bristle.action.ActionExecutionStage;
import pl.bristleback.server.bristle.action.ActionInformation;
import pl.bristleback.server.bristle.api.action.ActionInterceptor;
import pl.bristleback.server.bristle.api.action.ActionInterceptorContextResolver;
import pl.bristleback.server.bristle.api.annotations.Interceptor;
import pl.bristleback.server.bristle.security.authentication.AuthenticationsContainer;
import pl.bristleback.server.bristle.security.authentication.UserAuthentication;
import pl.bristleback.server.bristle.security.exception.AuthorizationException;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Prototype of Bristleback built in authorization interceptor.
 * This interceptor uses custom intercepting annotation, {@link pl.bristleback.server.bristle.api.annotations.Authorized}.
 * <p/>
 * Created on: 09.02.13 18:49 <br/>
 *
 * @author Wojciech Niemiec
 */
@Interceptor(stages = ActionExecutionStage.ACTION_EXTRACTION)
public class AuthorizationInterceptor implements ActionInterceptor<RequiredRights> {

  @Inject
  @Named("bristleAuthenticationsContainer")
  private AuthenticationsContainer authenticationsContainer;

  @Inject
  private AuthorizationInterceptorContextResolver authorizationInterceptorContextResolver;

  @Override
  public void intercept(ActionInformation actionInformation, ActionExecutionContext context, RequiredRights requiredRights) {
    UserAuthentication authentication = authenticationsContainer.getAuthentication(context.getUserContext().getId());
    for (String requiredRight : requiredRights.getRequiredRights()) {
      if (!authentication.getAuthenticatedUser().getAuthorities().contains(requiredRight)) {
        throw new AuthorizationException(authentication.getAuthenticatedUser().getUsername(), requiredRight);
      }
    }
  }

  @Override
  public ActionInterceptorContextResolver<RequiredRights> getContextResolver() {
    return authorizationInterceptorContextResolver;
  }
}
