package pl.bristleback.server.bristle.action.interceptor;

import pl.bristleback.server.bristle.action.ActionExecutionStage;
import pl.bristleback.server.bristle.api.action.ActionInterceptor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * //@todo class description
 * <p/>
 * Created on: 20.01.13 12:04 <br/>
 *
 * @author Wojciech Niemiec
 */
public class ActionInterceptors {

  private Map<ActionExecutionStage, List<ActionInterceptor>> interceptors = new HashMap<ActionExecutionStage, List<ActionInterceptor>>();

  public ActionInterceptors(Map<ActionExecutionStage, List<ActionInterceptor>> interceptors) {
    this.interceptors = interceptors;
  }

  public List<ActionInterceptor> getInterceptorsForStage(ActionExecutionStage stage) {
    return interceptors.get(stage);
  }
}
