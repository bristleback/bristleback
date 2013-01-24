package pl.bristleback.server.bristle.conf.resolver.action;

import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.action.ActionExecutionStage;
import pl.bristleback.server.bristle.action.interceptor.ActionInterceptors;
import pl.bristleback.server.bristle.api.action.ActionInterceptor;
import pl.bristleback.server.bristle.api.annotations.Interceptor;
import pl.bristleback.server.bristle.exceptions.BristleInitializationException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * //@todo class description
 * <p/>
 * Created on: 24.01.13 20:09 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component
public class ActionInterceptorsSorter {

  public ActionInterceptors sortInterceptors(List<ActionInterceptor> allInterceptors) {
    Map<ActionExecutionStage, List<ActionInterceptor>> interceptorsMap = new HashMap<ActionExecutionStage, List<ActionInterceptor>>();
    for (ActionExecutionStage executionStage : ActionExecutionStage.values()) {
      List<ActionInterceptor> interceptorsForStage = chooseInterceptorsForStage(executionStage, allInterceptors);
      interceptorsMap.put(executionStage, interceptorsForStage);
    }
    return new ActionInterceptors(interceptorsMap);
  }

  private List<ActionInterceptor> chooseInterceptorsForStage(ActionExecutionStage stage, List<ActionInterceptor> allInterceptors) {
    List<ActionInterceptor> interceptorsForStage = new ArrayList<ActionInterceptor>();
    for (ActionInterceptor interceptor : allInterceptors) {
      if (!interceptor.getClass().isAnnotationPresent(Interceptor.class)) {
        throw new BristleInitializationException("Interceptor classes must be annotated with " + Interceptor.class);
      }
      Interceptor interceptorAnnotation = interceptor.getClass().getAnnotation(Interceptor.class);
      for (ActionExecutionStage stageApplicableForInterceptor : interceptorAnnotation.stages()) {
        if (stageApplicableForInterceptor.equals(stage)) {
          interceptorsForStage.add(interceptor);
        }
      }
    }
    return interceptorsForStage;
  }
}
