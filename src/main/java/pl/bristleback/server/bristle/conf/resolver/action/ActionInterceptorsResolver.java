package pl.bristleback.server.bristle.conf.resolver.action;

import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.action.ActionClassInformation;
import pl.bristleback.server.bristle.action.ActionInformation;
import pl.bristleback.server.bristle.action.interceptor.ActionInterceptorInformation;
import pl.bristleback.server.bristle.action.interceptor.ActionInterceptors;
import pl.bristleback.server.bristle.api.action.ActionInterceptor;
import pl.bristleback.server.bristle.api.annotations.Intercept;
import pl.bristleback.server.bristle.exceptions.BristleInitializationException;
import pl.bristleback.server.bristle.integration.spring.BristleSpringIntegration;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * //@todo class description
 * <p/>
 * Created on: 24.01.13 17:35 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component
public class ActionInterceptorsResolver {

  @Inject
  private BristleSpringIntegration springIntegration;

  @Inject
  private ActionInterceptorsSorter actionInterceptorsSorter;

  public void resolveInterceptors(ActionClassInformation actionClassInformation) {
    List<ActionInterceptorInformation> interceptorsForClass = resolveInterceptorsForClass(actionClassInformation);
    for (ActionInformation actionInformation : actionClassInformation.getActions().values()) {
      ActionInterceptors sortedInterceptors = resolveInterceptorsList(interceptorsForClass, actionInformation);
      actionInformation.setActionInterceptors(sortedInterceptors);
    }
  }

  private ActionInterceptors resolveInterceptorsList(List<ActionInterceptorInformation> interceptorsForClass, ActionInformation actionInformation) {
    List<ActionInterceptorInformation> interceptorsForAction = resolveInterceptorsForAction(actionInformation);
    List<ActionInterceptorInformation> allInterceptors = new ArrayList<ActionInterceptorInformation>(interceptorsForAction);
    allInterceptors.addAll(interceptorsForClass);
    return actionInterceptorsSorter.sortInterceptors(allInterceptors);
  }

  private List<ActionInterceptorInformation> resolveInterceptorsForClass(ActionClassInformation actionClassInformation) {
    return resolveInterceptorsFromAnnotation(actionClassInformation.getType().getAnnotation(Intercept.class));
  }

  private List<ActionInterceptorInformation> resolveInterceptorsForAction(ActionInformation actionInformation) {
    return resolveInterceptorsFromAnnotation(actionInformation.getMethod().getAnnotation(Intercept.class));
  }

  private List<ActionInterceptorInformation> resolveInterceptorsFromAnnotation(Intercept interceptAnnotation) {
    if (interceptAnnotation == null) {
      return Collections.emptyList();
    }
    List<ActionInterceptorInformation> interceptors = new ArrayList<ActionInterceptorInformation>();
    for (Class<? extends ActionInterceptor> interceptorClass : interceptAnnotation.value()) {
      ActionInterceptor interceptor = springIntegration.getBean(interceptorClass);
      if (interceptor == null) {
        throw new BristleInitializationException("Cannot find interceptor Spring bean of type: " + interceptorClass);
      }
      ActionInterceptorInformation interceptorInformation = createActionInterceptorInformation(interceptor);
      interceptors.add(interceptorInformation);
    }
    return interceptors;
  }

  private ActionInterceptorInformation createActionInterceptorInformation(ActionInterceptor interceptor) {
    return new ActionInterceptorInformation(interceptor);
  }

}
