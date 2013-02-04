package pl.bristleback.server.bristle.conf.resolver.action;

import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.action.ActionExecutionStage;
import pl.bristleback.server.bristle.action.interceptor.ActionInterceptorInformation;
import pl.bristleback.server.bristle.action.interceptor.ActionInterceptors;
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

  public ActionInterceptors sortInterceptors(List<ActionInterceptorInformation> allInterceptors) {
    Map<ActionExecutionStage, List<ActionInterceptorInformation>> interceptorsMap = new HashMap<ActionExecutionStage, List<ActionInterceptorInformation>>();
    for (ActionExecutionStage executionStage : ActionExecutionStage.values()) {
      List<ActionInterceptorInformation> interceptorsForStage = chooseInterceptorsForStage(executionStage, allInterceptors);
      interceptorsMap.put(executionStage, interceptorsForStage);
    }
    return new ActionInterceptors(interceptorsMap);
  }

  private List<ActionInterceptorInformation> chooseInterceptorsForStage(ActionExecutionStage stage, List<ActionInterceptorInformation> allInterceptors) {
    List<ActionInterceptorInformation> interceptorsForStage = new ArrayList<ActionInterceptorInformation>();
    for (ActionInterceptorInformation interceptorInformation : allInterceptors) {
      Class<?> interceptorClass = interceptorInformation.getInterceptorInstance().getClass();
      if (!interceptorClass.isAnnotationPresent(Interceptor.class)) {
        throw new BristleInitializationException("Interceptor classes must be annotated with " + Interceptor.class);
      }
      Interceptor interceptorAnnotation = interceptorClass.getAnnotation(Interceptor.class);
      for (ActionExecutionStage stageApplicableForInterceptor : interceptorAnnotation.stages()) {
        if (stageApplicableForInterceptor.equals(stage)) {
          interceptorsForStage.add(interceptorInformation);
        }
      }
    }
    return interceptorsForStage;
  }
}
