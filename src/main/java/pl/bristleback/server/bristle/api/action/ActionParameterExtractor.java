package pl.bristleback.server.bristle.api.action;

import pl.bristleback.server.bristle.action.ActionParameterInformation;
import pl.bristleback.server.bristle.api.ConfigurationAware;
import pl.bristleback.server.bristle.api.users.IdentifiedUser;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-07-14 15:11:28 <br/>
 *
 * @author Wojciech Niemiec
 */
public interface ActionParameterExtractor<T> extends ConfigurationAware {

  T fromTextContent(String text, ActionParameterInformation parameterInformation, IdentifiedUser user) throws Exception;

  boolean isDeserializationRequired();
}
