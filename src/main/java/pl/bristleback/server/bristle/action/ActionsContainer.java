package pl.bristleback.server.bristle.action;

import pl.bristleback.server.bristle.action.exception.BrokenActionProtocolException;
import pl.bristleback.server.bristle.integration.spring.BristleSpringIntegration;

import java.util.Map;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-01-23 20:02:06 <br/>
 *
 * @author Wojciech Niemiec
 */
public class ActionsContainer {

  private Map<String, ActionClassInformation> actionClasses;

  public ActionClassInformation getActionClass(String actionClassName) {
    ActionClassInformation actionClass = actionClasses.get(actionClassName);
    if (actionClass == null) {
      throw new BrokenActionProtocolException(BrokenActionProtocolException.ReasonType.NO_ACTION_CLASS_FOUND, "Cannot find action class with name \"" + actionClassName + "\".");
    }
    return actionClass;
  }

  public Object getActionClassInstance(ActionClassInformation actionClass, BristleSpringIntegration springIntegration) {
    if (actionClass.isSingleton()) {
      return actionClass.getSingletonActionClassInstance();
    } else {
      return springIntegration.getApplicationBean(actionClass.getSpringBeanName(), actionClass.getType());
    }
  }

  public void setActionClasses(Map<String, ActionClassInformation> actionClasses) {
    this.actionClasses = actionClasses;
  }
}
