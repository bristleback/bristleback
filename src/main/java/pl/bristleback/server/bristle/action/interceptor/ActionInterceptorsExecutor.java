package pl.bristleback.server.bristle.action.interceptor;

import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.action.ActionExecutionContext;
import pl.bristleback.server.bristle.action.ActionInformation;

import java.util.List;

/**
 * //@todo class description
 * <p/>
 * Created on: 20.01.13 11:58 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component
public class ActionInterceptorsExecutor {

  public void executeInterceptorPolicy(ActionInformation actionInformation, ActionExecutionContext context) {
    List<ActionInterceptorInformation> interceptors = actionInformation.getActionInterceptors().getInterceptorsForStage(context.getStage());
    if (interceptors.isEmpty()) {
      return;
    }
    for (ActionInterceptorInformation interceptorInformation : interceptors) {
      interceptorInformation.getInterceptorInstance().intercept(actionInformation, context);
    }
  }
}
