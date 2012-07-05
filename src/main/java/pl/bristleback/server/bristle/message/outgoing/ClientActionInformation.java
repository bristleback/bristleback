package pl.bristleback.server.bristle.message.outgoing;

import org.apache.log4j.Logger;
import pl.bristleback.server.bristle.action.ActionParameterInformation;

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

  private String name;

  private List<ActionParameterInformation> parameters;

  public ClientActionInformation(String name, List<ActionParameterInformation> parameters) {
    this.name = name;
    this.parameters = parameters;
  }

  public List<ActionParameterInformation> getParameters() {
    return parameters;
  }

  public String getName() {
    return name;
  }
}
