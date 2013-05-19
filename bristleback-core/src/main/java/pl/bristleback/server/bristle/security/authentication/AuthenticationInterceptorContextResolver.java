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

import pl.bristleback.server.bristle.action.ActionInformation;
import pl.bristleback.server.bristle.api.action.ActionInterceptorContextResolver;

/**
 * This interceptor context resolver checks if action returns
 * {@link pl.bristleback.server.bristle.api.users.UserDetails} implementation.
 * It also checks and processes action parameters, for example if password should be encoded.
 * <p/>
 * Created on: 17.02.13 10:42 <br/>
 *
 * @author Wojciech Niemiec
 */
public class AuthenticationInterceptorContextResolver implements ActionInterceptorContextResolver<AuthenticationOperationContext> {

  @Override
  public AuthenticationOperationContext resolveInterceptorContext(ActionInformation actionInformation) {
    //TODO-do what this class is supposed to do
    return new AuthenticationOperationContext();
  }
}
