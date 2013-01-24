package pl.bristleback.server.bristle.api.action;

import pl.bristleback.server.bristle.action.ActionExecutionContext;
import pl.bristleback.server.bristle.action.ActionInformation;

/**
 * //@todo class description
 * <p/>
 * Created on: 20.01.13 11:52 <br/>
 *
 * @author Wojciech Niemiec
 */
public interface ActionInterceptor {

  void intercept(ActionInformation actionInformation, ActionExecutionContext context);
}
