package pl.bristleback.server.bristle.action.client;

import org.apache.log4j.Logger;
import pl.bristleback.server.bristle.api.annotations.ClientAction;
import pl.bristleback.server.bristle.exceptions.ClientActionException;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-05-26 20:13:14 <br/>
 *
 * @author Wojciech Niemiec
 */
public class ClientActionClassInformation {
  private static Logger log = Logger.getLogger(ClientActionClassInformation.class.getName());

  private String name;

  private Map<String, ClientActionInformation> clientActions;

  public ClientActionClassInformation(String name, Map<String, ClientActionInformation> clientActions) {
    this.name = name;
    this.clientActions = clientActions;
  }

  public ClientActionInformation getClientAction(String actionName) {
    ClientActionInformation actionInformation = clientActions.get(actionName);
    if (actionInformation == null) {
      throw new ClientActionException("Action with name " + actionName + " cannot be found");
    }
    return actionInformation;
  }

  public String getName() {
    return name;
  }

  public ClientActionInformation getClientAction(Method action) {
    String actionName = getActionName(action);
    return getClientAction(actionName);
  }

  private String getActionName(Method action) {
    ClientAction actionAnnotation = action.getAnnotation(ClientAction.class);
    if (actionAnnotation != null) {
      return actionAnnotation.value();
    }
    return action.getName();
  }

  public Map<String, ClientActionInformation> getClientActions() {
    return clientActions;
  }
}
