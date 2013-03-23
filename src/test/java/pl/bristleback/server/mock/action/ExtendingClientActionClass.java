package pl.bristleback.server.mock.action;

import pl.bristleback.server.bristle.api.annotations.ClientAction;
import pl.bristleback.server.bristle.api.annotations.ClientActionClass;
import pl.bristleback.server.bristle.api.users.UserContext;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-07-22 22:30:23 <br/>
 *
 * @author Wojciech Niemiec
 */
@ClientActionClass
public class ExtendingClientActionClass extends NonStandardClientActionClass {

  /**
   * Overriding action
   *
   * @param user user
   * @return user
   */
  @ClientAction
  public UserContext someAction(UserContext user) {
    return user;
  }

  @ClientAction
  public UserContext additionalAction(UserContext user) {
    return user;
  }
}
