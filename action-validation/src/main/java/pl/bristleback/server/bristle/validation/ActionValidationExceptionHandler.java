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

package pl.bristleback.server.bristle.validation;

import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.action.ActionExecutionContext;
import pl.bristleback.server.bristle.action.ActionExecutionStage;
import pl.bristleback.server.bristle.api.action.ActionExceptionHandler;

import javax.validation.ConstraintViolation;
import java.util.ArrayList;
import java.util.List;

@Component
public class ActionValidationExceptionHandler implements ActionExceptionHandler<ActionValidationException> {

  @Override
  public Object handleException(ActionValidationException exception, ActionExecutionContext context) {
    List<ActionConstraintViolation> violations = new ArrayList<ActionConstraintViolation>();
    for (ConstraintViolation<Object> constraintViolation : exception.getValidationResults()) {
      violations.add(new ActionConstraintViolation(constraintViolation.getPropertyPath().toString(),
        constraintViolation.getMessageTemplate(), constraintViolation.getMessage()));
    }
    return new ValidationExceptionResponse(violations);
  }

  @Override
  public ActionExecutionStage[] getHandledStages() {
    return new ActionExecutionStage[]{ActionExecutionStage.PARAMETERS_EXTRACTION};
  }
}
