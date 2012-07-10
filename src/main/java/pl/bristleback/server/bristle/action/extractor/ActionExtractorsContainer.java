package pl.bristleback.server.bristle.action.extractor;

import org.apache.log4j.Logger;
import pl.bristleback.server.bristle.api.action.ActionParameterExtractor;
import pl.bristleback.server.bristle.utils.ReflectionUtils;

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
    return ReflectionUtils.chooseBestClassStrategy(parameterExtractors, parameterClass);
  }

  public void setParameterExtractors(Map<Class, ActionParameterExtractor> parameterExtractors) {
    this.parameterExtractors = parameterExtractors;
  }
}
