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

package pl.bristleback.server.bristle.security.exception.handler;

import pl.bristleback.server.bristle.action.ActionExecutionContext;
import pl.bristleback.server.bristle.action.ActionExecutionStage;
import pl.bristleback.server.bristle.api.action.ActionExceptionHandler;
import pl.bristleback.server.bristle.security.exception.AuthorizationException;
import pl.bristleback.server.bristle.security.exception.response.AuthorizationExceptionResponse;

/**
 * This handler prepares {@link AuthorizationExceptionResponse} exception response.
 * <p/>
 * Created on: 30.03.13 14:08 <br/>
 *
 * @author Wojciech Niemiec
 */
public class AuthorizationExceptionHandler implements ActionExceptionHandler<AuthorizationException> {

  @Override
  public Object handleException(AuthorizationException exception, ActionExecutionContext context) {
    return new AuthorizationExceptionResponse(exception.getUsername(), exception.getMissingAuthority());
  }

  @Override
  public ActionExecutionStage[] getHandledStages() {
    return ActionExecutionStage.values();
  }
}
