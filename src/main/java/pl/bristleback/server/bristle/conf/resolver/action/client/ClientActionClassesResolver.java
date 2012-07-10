package pl.bristleback.server.bristle.conf.resolver.action.client;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.action.client.ClientActionClassInformation;
import pl.bristleback.server.bristle.action.client.ClientActionInformation;
import pl.bristleback.server.bristle.api.annotations.ClientAction;
import pl.bristleback.server.bristle.api.annotations.ClientActionClass;
import pl.bristleback.server.bristle.integration.spring.BristleSpringIntegration;

import javax.inject.Inject;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-07-05 09:54:10 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component
public class ClientActionClassesResolver {

  @Inject
  private BristleSpringIntegration springIntegration;

  @Inject
  private ClientActionResolver clientActionResolver;

  public Map<String, ClientActionClassInformation> resolve() {
    Map<String, ClientActionClassInformation> actionClasses = new HashMap<String, ClientActionClassInformation>();

    Map<String, Object> foundActions = springIntegration.getActualContext().getBeansWithAnnotation(ClientActionClass.class);
    for (Map.Entry<String, Object> actionClassEntry : foundActions.entrySet()) {
      Object actionClass = actionClassEntry.getValue();
      ClientActionClassInformation actionClassInformation = prepareActionClass(actionClass);
      actionClasses.put(actionClassInformation.getName(), actionClassInformation);
    }

    return actionClasses;
  }

  private ClientActionClassInformation prepareActionClass(Object actionClass) {
    ClientActionClass actionClassAnnotation = AnnotationUtils.findAnnotation(actionClass.getClass(), ClientActionClass.class);
    String actionClassName = getActionClassName(actionClass.getClass(), actionClassAnnotation);
    Map<String, ClientActionInformation> actions = prepareActions(actionClass.getClass(), actionClassName);

    return new ClientActionClassInformation(actionClassName, actions);
  }

  private String getActionClassName(Class<?> actionClass, ClientActionClass actionClassAnnotation) {
    if (StringUtils.isNotBlank(actionClassAnnotation.name())) {
      return actionClassAnnotation.name();
    }
    return actionClass.getSuperclass().getSimpleName(); // skip proxy class
  }

  private Map<String, ClientActionInformation> prepareActions(Class<?> actionClass, String actionClassName) {
    Map<String, ClientActionInformation> actionsMap = new HashMap<String, ClientActionInformation>();
    for (Method action : actionClass.getMethods()) {
      ClientAction actionClassAnnotation = AnnotationUtils.findAnnotation(action, ClientAction.class);
      if (actionClassAnnotation != null) {
        ClientActionInformation actionInformation = clientActionResolver.prepareActionInformation(actionClassName, action);
        actionsMap.put(actionInformation.getName(), actionInformation);
      }
    }
    return actionsMap;
  }
}
