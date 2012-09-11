package pl.bristleback.server.bristle.action;

import org.apache.commons.collections.CollectionUtils;
import pl.bristleback.server.bristle.action.response.ActionResponseInformation;
import pl.bristleback.server.bristle.api.action.ActionInformation;

import java.util.Iterator;
import java.util.List;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-07-20 17:56:33 <br/>
 *
 * @author Wojciech Niemiec
 */
public abstract class AbstractActionInformation<T> implements ActionInformation<T> {

  private String name;

  private List<ActionParameterInformation> parameters;
  private ActionResponseInformation responseInformation;

  protected AbstractActionInformation(String name) {
    this.name = name;
  }

  public boolean isDefaultAction() {
    return false;
  }

  public String getName() {
    return name;
  }

  public List<ActionParameterInformation> getParameters() {
    return parameters;
  }

  public void setParameters(List<ActionParameterInformation> parameters) {
    this.parameters = parameters;
  }

  @Override
  public String toString() {
    StringBuilder paramsToString = new StringBuilder();
    if (CollectionUtils.isNotEmpty(parameters)) {
      Iterator<ActionParameterInformation> it = parameters.iterator();
      while (it.hasNext()) {
        paramsToString.append(it.next().toString());
        if (it.hasNext()) {
          paramsToString.append(", ");
        }
      }
    }

    return "action: " + name + "(" + paramsToString.toString() + ")";
  }

  public ActionResponseInformation getResponseInformation() {
    return responseInformation;
  }

  public void setResponseInformation(ActionResponseInformation responseInformation) {
    this.responseInformation = responseInformation;
  }
}