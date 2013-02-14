package pl.bristleback.server.mock.action;

import pl.bristleback.server.bristle.api.annotations.ClientAction;
import pl.bristleback.server.bristle.api.annotations.ClientActionClass;
import pl.bristleback.server.bristle.api.users.IdentifiedUser;

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
  public IdentifiedUser someAction(IdentifiedUser user) {
    return user;
  }

  @ClientAction
  public IdentifiedUser additionalAction(IdentifiedUser user) {
    return user;
  }
}
