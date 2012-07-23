package pl.bristleback.server.bristle.action.client;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.annotation.AnnotationUtils;
import pl.bristleback.server.bristle.api.annotations.ClientAction;

import java.lang.reflect.Method;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-07-23 16:40:22 <br/>
 *
 * @author Wojciech Niemiec
 */
public final class ClientActionUtils {
  private ClientActionUtils() {
    throw new UnsupportedOperationException();
  }

  public static String resolveActionName(Method actionMethod) {
    ClientAction actionAnnotation = AnnotationUtils.findAnnotation(actionMethod, ClientAction.class);
    if (StringUtils.isNotBlank(actionAnnotation.value())) {
      return actionAnnotation.value();
    }
    return actionMethod.getName();
  }

  public static String resolveFullName(String actionName, String actionClassName) {
    return actionClassName + pl.bristleback.server.bristle.utils.StringUtils.DOT_AS_STRING + actionName;
  }
}

