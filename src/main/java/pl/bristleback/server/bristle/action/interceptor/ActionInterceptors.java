package pl.bristleback.server.bristle.action.interceptor;

import pl.bristleback.server.bristle.action.ActionExecutionStage;

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

  private Map<ActionExecutionStage, List<ActionInterceptorInformation>> interceptors = new HashMap<ActionExecutionStage, List<ActionInterceptorInformation>>();

  public ActionInterceptors(Map<ActionExecutionStage, List<ActionInterceptorInformation>> interceptors) {
    this.interceptors = interceptors;
  }

  public List<ActionInterceptorInformation> getInterceptorsForStage(ActionExecutionStage stage) {
    return interceptors.get(stage);
  }
}
