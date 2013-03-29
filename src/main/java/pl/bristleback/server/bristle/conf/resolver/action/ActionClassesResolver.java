package pl.bristleback.server.bristle.conf.resolver.action;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.action.ActionClassInformation;
import pl.bristleback.server.bristle.action.ActionInformation;
import pl.bristleback.server.bristle.action.ActionsContainer;
import pl.bristleback.server.bristle.api.annotations.Action;
import pl.bristleback.server.bristle.api.annotations.ActionClass;
import pl.bristleback.server.bristle.conf.resolver.action.interceptor.ActionInterceptorsResolver;
import pl.bristleback.server.bristle.action.exception.ActionInitializationException;
import pl.bristleback.server.bristle.integration.spring.BristleSpringIntegration;
import pl.bristleback.server.bristle.utils.PropertyUtils;

import javax.inject.Inject;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-07-22 09:54:10 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component
public class ActionClassesResolver {

  @Inject
  private ActionResolver actionResolver;

  @Inject
  private BristleSpringIntegration springIntegration;

  @Inject
  private ActionInterceptorsResolver actionInterceptorsResolver;

  public ActionsContainer resolve() {
    ActionsContainer actionsContainer = new ActionsContainer();
    Map<String, ActionClassInformation> actionClasses = new HashMap<String, ActionClassInformation>();
    Map<String, Object> foundActions = springIntegration.getBeansWithAnnotation(ActionClass.class);
    for (Map.Entry<String, Object> actionClassEntry : foundActions.entrySet()) {
      String actionClassBeanName = actionClassEntry.getKey();
      Object actionClass = actionClassEntry.getValue();
      ActionClassInformation actionClassInformation = prepareActionClassInformation(actionClass, actionClassBeanName);
      actionClasses.put(actionClassInformation.getName(), actionClassInformation);
      actionInterceptorsResolver.resolveInterceptors(actionClassInformation);
    }
    actionsContainer.setActionClasses(actionClasses);
    return actionsContainer;
  }

  private ActionClassInformation prepareActionClassInformation(Object actionClass, String actionClassBeanName) {
    ActionClassInformation actionClassInformation = prepareActionClass(actionClass, actionClassBeanName);
    prepareActions(actionClass, actionClassInformation);
    return actionClassInformation;
  }

  private ActionClassInformation prepareActionClass(Object actionClass, String actionClassBeanName) {
    ActionClassInformation actionClassInformation = new ActionClassInformation();
    ActionClass actionClassAnnotation = actionClass.getClass().getAnnotation(ActionClass.class);
    String actionClassName = resolveActionClassName(actionClass, actionClassAnnotation);
    actionClassInformation.setName(actionClassName);
    actionClassInformation.setSpringBeanName(actionClassBeanName);
    actionClassInformation.setType(actionClass.getClass());
    actionClassInformation.setSingleton(springIntegration.isSingleton(actionClassBeanName));

    if (actionClassInformation.isSingleton()) {
      actionClassInformation.setSingletonActionClassInstance(actionClass);
    }
    return actionClassInformation;
  }

  private String resolveActionClassName(Object actionClass, ActionClass actionClassAnnotation) {
    String actionClassName = actionClassAnnotation.name();
    if (StringUtils.isBlank(actionClassName)) {
      actionClassName = actionClass.getClass().getSimpleName();
    }
    return actionClassName;
  }

  private void prepareActions(Object actionClass, ActionClassInformation actionClassInformation) {
    List<Method> actions = PropertyUtils.getMethodsAnnotatedWith(actionClass.getClass(), Action.class, true);
    for (Method action : actions) {
      ActionInformation actionInformation = actionResolver.prepareActionInformation(actionClass.getClass(), action);
      addCreatedAction(actionClassInformation, actionInformation);
    }
  }

  private void addCreatedAction(ActionClassInformation actionClassInformation, ActionInformation actionInformation) {
    actionInformation.setActionClass(actionClassInformation);
    if (actionInformation.isDefaultAction()) {
      actionClassInformation.setDefaultAction(actionInformation);
    }
    if (actionClassInformation.getActions().containsKey(actionInformation.getName())) {
      throw new ActionInitializationException("Currently, multiple methods with the same name in one action class are not allowed");
    }
    actionClassInformation.getActions().put(actionInformation.getName(), actionInformation);
  }


}