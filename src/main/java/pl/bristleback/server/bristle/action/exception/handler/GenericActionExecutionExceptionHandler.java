package pl.bristleback.server.bristle.action.exception.handler;

import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.action.ActionExecutionContext;
import pl.bristleback.server.bristle.action.ActionExecutionStage;
import pl.bristleback.server.bristle.action.response.ExceptionResponse;
import pl.bristleback.server.bristle.api.action.ActionExceptionHandler;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-07-23 19:46:16 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component
public class GenericActionExecutionExceptionHandler implements ActionExceptionHandler<Exception> {

  @Override
  public Object handleException(Exception e, ActionExecutionContext context) {
    return new ExceptionResponse(e.getClass().getSimpleName(), context.getStage());
  }

  @Override
  public ActionExecutionStage[] getHandledStages() {
    return new ActionExecutionStage[]{
      ActionExecutionStage.PARAMETERS_EXTRACTION,
      ActionExecutionStage.ACTION_EXECUTION};
  }
}
