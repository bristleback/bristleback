package pl.bristleback.server.bristle.api.action;

import pl.bristleback.server.bristle.action.ActionExecutionContext;
import pl.bristleback.server.bristle.action.ActionParameterInformation;
import pl.bristleback.server.bristle.api.ConfigurationAware;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-07-14 15:11:28 <br/>
 *
 * @author Wojciech Niemiec
 */
public interface ActionParameterExtractor<T> extends ConfigurationAware {

  T fromTextContent(String text, ActionParameterInformation parameterInformation, ActionExecutionContext context) throws Exception;

  boolean isDeserializationRequired();
}
