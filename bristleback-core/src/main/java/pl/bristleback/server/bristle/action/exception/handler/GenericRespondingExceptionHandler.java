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

package pl.bristleback.server.bristle.action.exception.handler;

import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.action.ActionExecutionContext;
import pl.bristleback.server.bristle.action.ActionExecutionStage;
import pl.bristleback.server.bristle.action.response.ExceptionResponse;
import pl.bristleback.server.bristle.api.action.ActionExceptionHandler;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-02-05 12:46:20 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component
public class GenericRespondingExceptionHandler implements ActionExceptionHandler<Exception> {

  @Override
  public Object handleException(Exception e, ActionExecutionContext context) {
    return new ExceptionResponse(context.getStage());
  }

  @Override
  public ActionExecutionStage[] getHandledStages() {
    return new ActionExecutionStage[]{
      ActionExecutionStage.ACTION_EXTRACTION,
      ActionExecutionStage.RESPONSE_CONSTRUCTION};
  }
}
