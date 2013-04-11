package pl.bristleback.server.bristle.action.exception.handler;

import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.action.ActionExecutionContext;
import pl.bristleback.server.bristle.action.ActionExecutionStage;
import pl.bristleback.server.bristle.api.action.ActionExceptionHandler;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-02-05 16:15:19 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component
public class VoidExceptionHandler implements ActionExceptionHandler<Exception> {

  public static final VoidExceptionHandler HANDLER = new VoidExceptionHandler();

  private static final ActionExecutionStage[] USAGE = new ActionExecutionStage[0];

  @Override
  public Void handleException(Exception e, ActionExecutionContext context) {
    return null;
  }

  @Override
  public ActionExecutionStage[] getHandledStages() {
    return USAGE;
  }
}
