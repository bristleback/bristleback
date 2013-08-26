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

package pl.bristleback.server.bristle.security;

import pl.bristleback.server.bristle.action.ActionExecutionContext;
import pl.bristleback.server.bristle.action.ActionParameterInformation;
import pl.bristleback.server.bristle.api.action.ActionParameterExtractor;
import pl.bristleback.server.bristle.api.users.UserDetails;
import pl.bristleback.server.bristle.security.authentication.AuthenticationsContainer;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * This action extractor returns instance of {@link UserDetails} implementation assigned for current connection.
 * If this connection is not authenticated, or authentication is not valid,
 * {@link pl.bristleback.server.bristle.security.exception.UserNotAuthenticatedException} exception is thrown.
 * <p/>
 * Created on: 13.04.13 17:50 <br/>
 *
 * @author Wojciech Niemiec
 */
public class UserDetailsParameterExtractor implements ActionParameterExtractor<UserDetails> {

  @Inject
  @Named("bristleAuthenticationsContainer")
  private AuthenticationsContainer authenticationsContainer;

  @Override
  public UserDetails fromTextContent(String text, ActionParameterInformation parameterInformation, ActionExecutionContext context) throws Exception {
    return authenticationsContainer.getAuthentication(context.getUserContext().getId()).getAuthenticatedUser();
  }

  @Override
  public boolean isDeserializationRequired() {
    return false;
  }
}
