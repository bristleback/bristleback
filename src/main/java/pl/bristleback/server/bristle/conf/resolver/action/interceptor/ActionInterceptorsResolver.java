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
import pl.bristleback.server.bristle.utils.AnnotationUtils;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.Arrays;
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
    ActionInterceptorContextResolver interceptorContextResolver = springIntegration.getBean(interceptorAnnotation.contextResolver());
    return new ActionInterceptorInformation(actionInterceptor, interceptorContextResolver, interceptorAnnotation.stages());
  }

  public void resolveInterceptors(ActionClassInformation actionClassInformation) {
    List<ActionInterceptorInformation> interceptorsInformationForClass = getInterceptorDescriptionsForClass(actionClassInformation);
    for (ActionInformation actionInformation : actionClassInformation.getActions().values()) {
      List<Class<? extends ActionInterceptor>> interceptorClasses = getInterceptorsUsed(actionInformation.getMethod());
      List<ActionInterceptorInformation> interceptorsInformationForAction = resolveInterceptorsInformation(interceptorClasses);
      interceptorsInformationForAction.addAll(interceptorsInformationForClass);
      List<InterceptionProcessContext> interceptorContexts = actionInterceptorContextsResolver
        .resolveContexts(interceptorsInformationForAction, actionInformation);
      ActionInterceptors sortedInterceptors = actionInterceptorsSorter.sortInterceptors(interceptorContexts);
      actionInformation.setActionInterceptors(sortedInterceptors);
    }
  }

  private List<Class<? extends ActionInterceptor>> getInterceptorsUsed(AnnotatedElement annotatedElement) {
    List<Class<? extends ActionInterceptor>> interceptorClasses = new ArrayList<Class<? extends ActionInterceptor>>();
    List<Intercept> interceptAnnotations = AnnotationUtils.findNestedAnnotations(annotatedElement, Intercept.class);
    for (Intercept interceptAnnotation : interceptAnnotations) {
      interceptorClasses.addAll(Arrays.asList(interceptAnnotation.value()));
    }
    return interceptorClasses;
  }

  private List<ActionInterceptorInformation> getInterceptorDescriptionsForClass(ActionClassInformation actionClassInformation) {
    List<Class<? extends ActionInterceptor>> interceptorClasses = getInterceptorsUsed(actionClassInformation.getType());
    return resolveInterceptorsInformation(interceptorClasses);
  }

  private List<ActionInterceptorInformation> resolveInterceptorsInformation(List<Class<? extends ActionInterceptor>>
                                                                              interceptorClasses) {
    List<ActionInterceptorInformation> interceptors = new ArrayList<ActionInterceptorInformation>();
    for (Class<? extends ActionInterceptor> interceptorClass : interceptorClasses) {
      if (!allInterceptors.containsKey(interceptorClass)) {
        throw new BristleInitializationException("Cannot find interceptor Spring bean of type: " + interceptorClass);
      }
      interceptors.add(allInterceptors.get(interceptorClass));
    }
    return interceptors;
  }

}
