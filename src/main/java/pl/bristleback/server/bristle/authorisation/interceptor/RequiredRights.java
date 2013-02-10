package pl.bristleback.server.bristle.authorisation.interceptor;

import java.util.List;

/**
 * List of user rights that are required to be able to invoke action or action class.
 * <p/>
 * Created on: 09.02.13 18:50 <br/>
 *
 * @author Wojciech Niemiec
 */
public class RequiredRights {

  private List<String> requiredRights;

  public RequiredRights(List<String> requiredRights) {
    this.requiredRights = requiredRights;
  }

  public List<String> getRequiredRights() {
    return requiredRights;
  }
}
