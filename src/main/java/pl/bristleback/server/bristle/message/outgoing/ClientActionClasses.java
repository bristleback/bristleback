package pl.bristleback.server.bristle.message.outgoing;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.api.annotations.Outgoing;
import pl.bristleback.server.bristle.exceptions.ClientActionException;

import java.util.Map;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-05-26 20:12:47 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component
public class ClientActionClasses {
  private static Logger log = Logger.getLogger(ClientActionClasses.class.getName());

  private Map<String, ClientActionClassInformation> actionClasses;

  public ClientActionClasses(Map<String, ClientActionClassInformation> actionClasses) {
    this.actionClasses = actionClasses;
  }

  public ClientActionClassInformation getOutgoingActionClass(Class<?> actionClass) {
    Outgoing outgoingAnnotation = actionClass.getAnnotation(Outgoing.class);
    if (outgoingAnnotation == null) {
      throw new ClientActionException(Outgoing.class.getSimpleName() + " annotation not found in " + actionClass.getName() + " class.");
    }
    String actionClassName = getActionClassName(outgoingAnnotation);

    return actionClasses.get(actionClassName);
  }

  private String getActionClassName(Outgoing outgoingAnnotation) {
    if (StringUtils.isNotBlank(outgoingAnnotation.actionClassName())) {
      return outgoingAnnotation.actionClassName();
    }
    if (!outgoingAnnotation.actionClass().equals(Object.class)) {
      return outgoingAnnotation.actionClass().getSimpleName();
    }
    throw new ClientActionException("Either action class or action class name must be provided");
  }

  public ClientActionClassInformation getOutgoingActionClass(String actionClassName) {
    return actionClasses.get(actionClassName);
  }
}
