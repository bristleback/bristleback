package sample.handlers;

import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.action.ActionExecutionContext;
import pl.bristleback.server.bristle.action.ActionExecutionStage;
import pl.bristleback.server.bristle.action.response.ExceptionResponse;
import pl.bristleback.server.bristle.api.action.ActionExceptionHandler;

@Component
public class NumberFormatBindingExceptionHandler implements ActionExceptionHandler<NumberFormatException> {

  @Override
  public Object handleException(NumberFormatException e, ActionExecutionContext context) {
    return new ExceptionResponse("sample.NumberFormatException");
  }

  @Override
  public ActionExecutionStage[] getHandledStages() {
    return new ActionExecutionStage[]{ActionExecutionStage.PARAMETERS_EXTRACTION};
  }
}
