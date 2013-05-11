package pl.bristleback.server.bristle.conf.resolver.action.interceptor;

import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.action.ActionExecutionStage;
import pl.bristleback.server.bristle.action.interceptor.ActionInterceptors;
import pl.bristleback.server.bristle.action.interceptor.InterceptionProcessContext;
import pl.bristleback.server.bristle.conf.resolver.action.IncreasingOrderSorter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class sorts and composes interceptors and returns {@link ActionInterceptors} object,
 * which represents all interceptors for single action, sorted by an action execution stage.
 * For each interceptors in the particular action stage, it is possible to control order of their
 * invocation by using the spring annotation {@link org.springframework.core.annotation.Order}.
 * Interceptors are sorted by following rules:
 * <ol>
 * <li>
 * Interceptors coming from {@link pl.bristleback.server.bristle.api.annotations.Intercept} annotation placed above action.
 * </li>
 * <li>
 * Interceptors coming from annotations placed above action, marked with {@link pl.bristleback.server.bristle.api.annotations.Intercept} annotation.
 * </li>
 * <li>
 * Interceptors coming from {@link pl.bristleback.server.bristle.api.annotations.Intercept} annotation placed above action class.
 * </li>
 * <li>
 * Interceptors coming from annotations placed above action class,
 * marked with {@link pl.bristleback.server.bristle.api.annotations.Intercept} annotation.
 * </li>
 * <li>
 * Interceptors from interceptor matchers defined in application configuration.
 * </li>
 * </ol>
 * <p/>
 * Created on: 24.01.13 20:09 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component
public class ActionInterceptorsSorter {

  public ActionInterceptors sortInterceptors(List<InterceptionProcessContext> allInterceptors) {
    Map<ActionExecutionStage, List<InterceptionProcessContext>> interceptorsMap = new HashMap<ActionExecutionStage, List<InterceptionProcessContext>>();
    for (ActionExecutionStage executionStage : ActionExecutionStage.values()) {
      List<InterceptionProcessContext> interceptorsForStage = chooseInterceptorsForStage(executionStage, allInterceptors);
      interceptorsMap.put(executionStage, interceptorsForStage);
    }
    return new ActionInterceptors(interceptorsMap);
  }

  private List<InterceptionProcessContext> chooseInterceptorsForStage(ActionExecutionStage stage, List<InterceptionProcessContext> allInterceptors) {
    List<InterceptionProcessContext> interceptorsForStage = new ArrayList<InterceptionProcessContext>();
    for (InterceptionProcessContext interceptionProcessContext : allInterceptors) {
      for (ActionExecutionStage stageApplicableForInterceptor : interceptionProcessContext.getInterceptorInformation().getStages()) {
        if (stageApplicableForInterceptor.equals(stage)) {
          interceptorsForStage.add(interceptionProcessContext);
        }
      }
    }
    sortInIncreasingOrder(interceptorsForStage);
    return interceptorsForStage;
  }

  private void sortInIncreasingOrder(List<InterceptionProcessContext> interceptorsForStage) {
    IncreasingOrderSorter<InterceptionProcessContext> sorter = createIncreasingOrderSorter();
    sorter.sort(interceptorsForStage);
  }

  private IncreasingOrderSorter<InterceptionProcessContext> createIncreasingOrderSorter() {
    return new IncreasingOrderSorter<InterceptionProcessContext>() {

      @Override
      public Class<?> getSortedClass(InterceptionProcessContext object) {
        return object.getInterceptorInformation().getInterceptorInstance().getClass();
      }
    };
  }
}
