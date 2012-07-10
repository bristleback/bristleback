package pl.bristleback.server.bristle.action.client;

import org.apache.log4j.Logger;
import pl.bristleback.server.bristle.action.ActionParameterInformation;
import pl.bristleback.server.bristle.api.action.ClientActionSender;

import java.util.List;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-05-27 08:38:39 <br/>
 *
 * @author Wojciech Niemiec
 */
public class ClientActionInformation {
  private static Logger log = Logger.getLogger(ClientActionInformation.class.getName());

  private String fullName;
  private String name;

  private List<ActionParameterInformation> parameters;
  private Object serialization;
  private ClientActionSender sender;

  public ClientActionInformation(String name, String fullName, Object serialization,
                                 List<ActionParameterInformation> parameters, ClientActionSender sender) {
    this.name = name;
    this.fullName = fullName;
    this.parameters = parameters;
    this.serialization = serialization;
    this.sender = sender;
  }

  public List<ActionParameterInformation> getParameters() {
    return parameters;
  }

  public String getName() {
    return name;
  }

  public String getFullName() {
    return fullName;
  }

  public Object getSerialization() {
    return serialization;
  }

  public ClientActionSender getResponse() {
    return sender;
  }
}
