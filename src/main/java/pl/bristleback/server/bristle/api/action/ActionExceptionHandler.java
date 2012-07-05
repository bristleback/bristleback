package pl.bristleback.server.bristle.api.action;

import pl.bristleback.server.bristle.action.ActionExecutionContext;
import pl.bristleback.server.bristle.action.ActionExecutionStage;
import pl.bristleback.server.bristle.api.BristlebackConfig;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-02-04 14:49:32 <br/>
 *
 * @author Wojciech Niemiec
 */
public interface ActionExceptionHandler<T extends Exception> {

  void init(BristlebackConfig configuration);

  Object handleException(T e, ActionExecutionContext context);

  ActionExecutionStage[] getHandledStages();
}
