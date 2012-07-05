package pl.bristleback.server.bristle.conf.resolver.action;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.action.AbstractActionInformation;
import pl.bristleback.server.bristle.action.ActionParameterInformation;
import pl.bristleback.server.bristle.action.response.ActionResponseInformation;
import pl.bristleback.server.bristle.action.DefaultActionInformation;
import pl.bristleback.server.bristle.action.MethodActionInformation;
import pl.bristleback.server.bristle.api.action.ActionInformation;
import pl.bristleback.server.bristle.api.action.DefaultAction;
import pl.bristleback.server.bristle.api.annotations.Action;

import javax.inject.Inject;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-07-23 12:56:33 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component
public class ActionResolver {
  private static Logger log = Logger.getLogger(ActionResolver.class.getName());

  @Inject
  private ParameterResolver parametersResolver;

  @Inject
  private ResponseResolver responseResolver;


  public ActionInformation prepareActionInformation(Class<?> clazz, Method action) {
    ActionInformation<?> actionInformation = prepareAction(clazz, action);

    List<ActionParameterInformation> parameters = new ArrayList<ActionParameterInformation>();
    for (int i = 0; i < action.getParameterTypes().length; i++) {
      Type parameterType = action.getGenericParameterTypes()[i];
      ActionParameterInformation parameterInformation =
        parametersResolver.prepareActionParameter(parameterType, action.getParameterAnnotations()[i]);
      parameters.add(parameterInformation);
    }
    actionInformation.setParameters(parameters);
    ActionResponseInformation responseInformation = responseResolver.resolveResponse(action);
    actionInformation.setResponseInformation(responseInformation);
    return actionInformation;
  }

  private ActionInformation<?> prepareAction(Class<?> clazz, Method action) {
    AbstractActionInformation<?> actionInformation;
    String actionName = getActionName(action);
    if (isDefaultRemoteAction(clazz, action)) {
      actionInformation = new DefaultActionInformation(actionName);
    } else {
      actionInformation = new MethodActionInformation(actionName, action);
    }
    return actionInformation;
  }

  private String getActionName(Method action) {
    Action actionAnnotation = action.getAnnotation(Action.class);
    if (actionAnnotation != null && StringUtils.isNotBlank(actionAnnotation.name())) {
      return actionAnnotation.name();
    } else {
      return action.getName();
    }
  }

  private boolean isDefaultRemoteAction(Class clazz, Method action) {
    if (!DefaultAction.class.isAssignableFrom(clazz)) {
      // this action class does not have default action
      return false;
    }
    Method defaultMethod = DefaultAction.class.getMethods()[0];
    if (!defaultMethod.getName().equals(action.getName())) {
      return false;
    }
    Class[] defaultParameterTypes = defaultMethod.getParameterTypes();
    if (defaultParameterTypes.length != action.getParameterTypes().length) {
      return false;
    }
    int parametersLength = defaultParameterTypes.length;
    for (int i = 0; i < parametersLength - 1; i++) {
      Class defaultParameterType = defaultParameterTypes[i];
      if (!defaultParameterType.isAssignableFrom(action.getParameterTypes()[i])) {
        return false;
      }
    }
    Type requiredLastParameterType = action.getGenericParameterTypes()[parametersLength - 1];
    Type actualLastParameterType = null;
    for (int i = 0; i < clazz.getInterfaces().length; i++) {
      Class interfaceOfClass = clazz.getInterfaces()[i];
      if (DefaultAction.class.equals(interfaceOfClass)) {
        Type genericInterface = clazz.getGenericInterfaces()[i];
        if (genericInterface instanceof ParameterizedType) {
          actualLastParameterType = ((ParameterizedType) (genericInterface)).getActualTypeArguments()[1];
        } else {
          actualLastParameterType = Object.class;
        }
      }
    }

    return requiredLastParameterType.equals(actualLastParameterType);
  }
}