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
