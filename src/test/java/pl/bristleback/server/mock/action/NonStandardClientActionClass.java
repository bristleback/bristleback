package pl.bristleback.server.mock.action;

import pl.bristleback.server.bristle.api.annotations.ClientAction;
import pl.bristleback.server.bristle.api.annotations.ClientActionClass;
import pl.bristleback.server.bristle.api.users.UserContext;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-07-22 11:56:41 <br/>
 *
 * @author Wojciech Niemiec
 */
@ClientActionClass(name = NonStandardClientActionClass.NAME)
public class NonStandardClientActionClass {

  public static final String NAME = "shortName";

  @ClientAction
  public UserContext someAction(UserContext user) {
    return user;
  }

  @ClientAction
  public UserContext anotherAction(UserContext user) {
    return user;
  }
}
