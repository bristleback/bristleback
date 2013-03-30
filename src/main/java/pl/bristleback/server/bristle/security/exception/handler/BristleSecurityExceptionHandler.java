package pl.bristleback.server.bristle.security.exception.handler;

import pl.bristleback.server.bristle.action.ActionExecutionContext;
import pl.bristleback.server.bristle.action.ActionExecutionStage;
import pl.bristleback.server.bristle.api.action.ActionExceptionHandler;
import pl.bristleback.server.bristle.security.exception.BristleSecurityException;
import pl.bristleback.server.bristle.security.exception.response.SecurityExceptionResponse;

/**
 * This handler defines responses for all exceptions inheriting from {@link BristleSecurityException}.
 * <p/>
 * Created on: 29.03.13 22:10 <br/>
 *
 * @author Wojciech Niemiec
 */
public class BristleSecurityExceptionHandler implements ActionExceptionHandler<BristleSecurityException> {

  @Override
  public Object handleException(BristleSecurityException exception, ActionExecutionContext context) {
    return new SecurityExceptionResponse(exception.getUsername(), exception.getClass().getSimpleName());
  }

  @Override
  public ActionExecutionStage[] getHandledStages() {
    return ActionExecutionStage.values();
  }
}
