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

package pl.bristleback.server.bristle.security.authentication;

import org.apache.log4j.Logger;
import pl.bristleback.server.bristle.action.ActionExecutionContext;
import pl.bristleback.server.bristle.action.ActionExecutionStage;
import pl.bristleback.server.bristle.action.ActionInformation;
import pl.bristleback.server.bristle.api.action.ActionInterceptor;
import pl.bristleback.server.bristle.api.annotations.Interceptor;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Logout interceptor handles logout process, invalidates current authentication
 * and removes it from concurrent authentications for this username.
 * <p/>
 * Created on: 18.02.13 20:57 <br/>
 *
 * @author Wojciech Niemiec
 */
@Interceptor(stages = ActionExecutionStage.ACTION_EXECUTION, contextResolver = AuthenticationInterceptorContextResolver.class)
public class LogoutInterceptor implements ActionInterceptor<AuthenticationOperationContext> {

  private static Logger log = Logger.getLogger(LogoutInterceptor.class.getName());

  @Inject
  @Named("bristleAuthenticationsContainer")
  private AuthenticationsContainer authenticationsContainer;

  @Inject
  @Named("bristleAuthenticationInformer")
  private AuthenticationInformer authenticationInformer;

  @Override
  public void intercept(ActionInformation actionInformation, ActionExecutionContext context, AuthenticationOperationContext interceptorContext) {
    String connectionId = context.getUserContext().getId();
    String username = authenticationsContainer.getAuthentication(connectionId).getAuthenticatedUser().getUsername();
    authenticationsContainer.logout(connectionId);
    authenticationInformer.sendLogoutInformation(context.getUserContext(), username, LogoutReason.REQUESTED_BY_CLIENT);
    log.debug("User \"" + username + "\" has been logged out.");
  }
}
