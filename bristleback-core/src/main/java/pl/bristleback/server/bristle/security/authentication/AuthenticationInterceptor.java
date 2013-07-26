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
import pl.bristleback.server.bristle.api.BristlebackConfig;
import pl.bristleback.server.bristle.api.ConfigurationAware;
import pl.bristleback.server.bristle.api.action.ActionInterceptor;
import pl.bristleback.server.bristle.api.action.ActionInterceptorContextResolver;
import pl.bristleback.server.bristle.api.annotations.Interceptor;
import pl.bristleback.server.bristle.api.users.UserDetails;
import pl.bristleback.server.bristle.security.exception.UserAlreadyAuthenticatedException;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * This class intercepts response from authenticating actions
 * and adds new authentication objects assigned for this connection.
 * If the number of concurrent connections authenticated as the same user as currently processed is exceeded,
 * the oldest one will be invalidated.
 * <p/>
 * Created on: 17.02.13 10:22 <br/>
 *
 * @author Wojciech Niemiec
 */
@Interceptor(stages = ActionExecutionStage.ACTION_EXECUTION)
public class AuthenticationInterceptor implements ActionInterceptor<AuthenticationOperationContext>, ConfigurationAware {

  private static Logger log = Logger.getLogger(AuthenticationInterceptor.class.getName());

  @Inject
  @Named("bristleAuthenticationsContainer")
  private AuthenticationsContainer authenticationsContainer;

  @Inject
  @Named("bristleAuthenticationInformer")
  private AuthenticationInformer authenticationInformer;

  @Inject
  private AuthenticationInterceptorContextResolver authenticationInterceptorContextResolver;

  @Override
  public void init(BristlebackConfig configuration) {
  }

  @Override
  public void intercept(ActionExecutionContext context, AuthenticationOperationContext interceptorContext) {
    UserDetails userDetails = (UserDetails) context.getResponse();
    if (authenticationsContainer.hasValidAuthenticationForConnection(context.getUserContext().getId())) {
      throw new UserAlreadyAuthenticatedException(userDetails.getUsername());
    }
    UserAuthentication userAuthentication = UserAuthentication.newValidAuthentication(context.getUserContext(), userDetails);
    authenticationsContainer.addAndInvalidatePreviousIfNecessary(userAuthentication);
    context.cancelResponseSending();
    authenticationInformer.sendAuthenticationSuccessInformation(context.getUserContext(), userDetails);
    log.debug("User \"" + userDetails.getUsername() + "\" has been successfully authenticated.");
  }

  @Override
  public ActionInterceptorContextResolver<AuthenticationOperationContext> getContextResolver() {
    return authenticationInterceptorContextResolver;
  }
}
