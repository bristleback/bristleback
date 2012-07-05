package pl.bristleback.server.bristle.action.extractor;

import org.apache.log4j.Logger;
import pl.bristleback.server.bristle.api.action.ActionParameterExtractor;
import pl.bristleback.server.bristle.exceptions.BristleInitializationException;

import java.util.Map;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-01-07 16:38:49 <br/>
 *
 * @author Wojciech Niemiec
 */
public class ActionExtractorsContainer {
  private static Logger log = Logger.getLogger(ActionExtractorsContainer.class.getName());

  private Map<Class, ActionParameterExtractor> parameterExtractors;

  public Map<Class, ActionParameterExtractor> getParameterExtractors() {
    return parameterExtractors;
  }

  public ActionParameterExtractor getParameterExtractor(Class parameterClass) {
    Class currentParameterClass = parameterClass;
    ActionParameterExtractor extractor = null;
    while (extractor == null) {
      extractor = parameterExtractors.get(currentParameterClass);
      if (extractor == null) {
        extractor = checkImplementedInterfaces(currentParameterClass);
      }
      if (extractor == null && currentParameterClass == Object.class) {
        throw new BristleInitializationException("Cannot retrieve message processor for type: " + parameterClass);
      }
      currentParameterClass = currentParameterClass.getSuperclass();
      if (currentParameterClass == null) {
        currentParameterClass = Object.class;
      }
    }
    return extractor;
  }

  private ActionParameterExtractor checkImplementedInterfaces(Class currentParameterClass) {
    Class[] interfaces = currentParameterClass.getInterfaces();
    for (Class interFace : interfaces) {
      if (parameterExtractors.containsKey(interFace)) {
        return parameterExtractors.get(interFace);
      }
    }
    return null;
  }

  public void setParameterExtractors(Map<Class, ActionParameterExtractor> parameterExtractors) {
    this.parameterExtractors = parameterExtractors;
  }
}
