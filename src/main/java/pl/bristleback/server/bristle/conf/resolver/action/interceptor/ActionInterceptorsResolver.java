package pl.bristleback.server.bristle.conf.resolver.action.interceptor;

import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.action.ActionClassInformation;
import pl.bristleback.server.bristle.action.ActionInformation;
import pl.bristleback.server.bristle.action.interceptor.ActionInterceptorInformation;
import pl.bristleback.server.bristle.action.interceptor.ActionInterceptors;
import pl.bristleback.server.bristle.action.interceptor.InterceptionProcessContext;
import pl.bristleback.server.bristle.api.action.ActionInterceptor;
import pl.bristleback.server.bristle.api.action.ActionInterceptorContextResolver;
import pl.bristleback.server.bristle.api.annotations.Intercept;
import pl.bristleback.server.bristle.api.annotations.Interceptor;
import pl.bristleback.server.bristle.exceptions.BristleInitializationException;
import pl.bristleback.server.bristle.integration.spring.BristleSpringIntegration;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is used for finding and preparing interceptions for each action class.
 * It also contains cached information about all interceptor components,
 * stored in {@link ActionInterceptorInformation} objects.
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

  @Inject
  private ActionInterceptorContextsResolver actionInterceptorContextsResolver;

  private Map<Class, ActionInterceptorInformation> allInterceptors;

  @PostConstruct
  private void loadAllInterceptors() {
    allInterceptors = new HashMap<Class, ActionInterceptorInformation>();
    Map<String, ActionInterceptor> interceptorBeans = springIntegration.getBeansOfType(ActionInterceptor.class);
    for (ActionInterceptor actionInterceptor : interceptorBeans.values()) {
      ActionInterceptorInformation interceptorInformation = loadInterceptor(actionInterceptor);
      allInterceptors.put(actionInterceptor.getClass(), interceptorInformation);
    }
  }

  private ActionInterceptorInformation loadInterceptor(ActionInterceptor actionInterceptor) {
    Class<?> interceptorClass = actionInterceptor.getClass();
    if (!interceptorClass.isAnnotationPresent(Interceptor.class)) {
      throw new BristleInitializationException("Interceptor classes must be annotated with " + Interceptor.class);
    }
    Interceptor interceptorAnnotation = interceptorClass.getAnnotation(Interceptor.class);
    if (!interceptorAnnotation.contextResolver().equals(ActionInterceptorContextResolver.class)) {
      ActionInterceptorContextResolver interceptorContextResolver = springIntegration.getBean(interceptorAnnotation.contextResolver());
      return new ActionInterceptorInformation(actionInterceptor, interceptorContextResolver, interceptorAnnotation.stages());
    } else {
      return new ActionInterceptorInformation(actionInterceptor, interceptorAnnotation.stages());
    }
  }

  public void resolveInterceptors(ActionClassInformation actionClassInformation) {
    List<ActionInterceptorInformation> interceptorsInformationForClass = getInterceptorDescriptionsForClass(actionClassInformation);
    for (ActionInformation actionInformation : actionClassInformation.getActions().values()) {
      Intercept interceptAnnotation = actionInformation.getMethod().getAnnotation(Intercept.class);
      List<ActionInterceptorInformation> interceptorsInformationForAction = resolveInterceptorsFromAnnotation(interceptAnnotation);
      interceptorsInformationForAction.addAll(interceptorsInformationForClass);
      List<InterceptionProcessContext> interceptorContexts = actionInterceptorContextsResolver
        .resolveContexts(interceptorsInformationForAction, actionInformation);
      ActionInterceptors sortedInterceptors = actionInterceptorsSorter.sortInterceptors(interceptorContexts);
      actionInformation.setActionInterceptors(sortedInterceptors);
    }
  }

  private List<ActionInterceptorInformation> getInterceptorDescriptionsForClass(ActionClassInformation actionClassInformation) {
    Intercept interceptAnnotation = actionClassInformation.getType().getAnnotation(Intercept.class);
    return resolveInterceptorsFromAnnotation(interceptAnnotation);
  }

  private List<ActionInterceptorInformation> resolveInterceptorsFromAnnotation(Intercept interceptAnnotation) {
    if (interceptAnnotation == null) {
      return new ArrayList<ActionInterceptorInformation>();
    }
    List<ActionInterceptorInformation> interceptors = new ArrayList<ActionInterceptorInformation>();
    for (Class<? extends ActionInterceptor> interceptorClass : interceptAnnotation.value()) {
      if (!allInterceptors.containsKey(interceptorClass)) {
        throw new BristleInitializationException("Cannot find interceptor Spring bean of type: " + interceptorClass);
      }
      interceptors.add(allInterceptors.get(interceptorClass));
    }
    return interceptors;
  }

}
