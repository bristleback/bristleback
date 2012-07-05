package pl.bristleback.server.bristle.action;

import org.apache.log4j.Logger;
import pl.bristleback.server.bristle.api.action.ActionParameterExtractor;
import pl.bristleback.server.bristle.api.users.IdentifiedUser;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-07-20 17:38:15 <br/>
 *
 * @author Wojciech Niemiec
 */
public class ActionParameterInformation {
  private static Logger log = Logger.getLogger(ActionParameterInformation.class.getName());

  private Object propertySerialization;
  private ActionParameterExtractor extractor;

  public Object resolveParameter(String message, IdentifiedUser user) throws Exception {
    return extractor.fromTextContent(message, this, user);
  }

  public ActionParameterExtractor getExtractor() {
    return extractor;
  }

  public void setExtractor(ActionParameterExtractor extractor) {
    this.extractor = extractor;
  }

  public Object getPropertySerialization() {
    return propertySerialization;
  }

  public void setPropertySerialization(Object propertySerialization) {
    this.propertySerialization = propertySerialization;
  }

  @Override
  public String toString() {
    return "Action parameter information, extractor: " + extractor + ", property Serialization: " + propertySerialization;
  }
}
