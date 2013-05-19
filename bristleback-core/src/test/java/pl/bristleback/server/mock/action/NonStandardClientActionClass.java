package pl.bristleback.server.mock.action;

import pl.bristleback.server.bristle.api.annotations.ClientAction;
import pl.bristleback.server.bristle.api.annotations.ClientActionClass;
import pl.bristleback.server.bristle.api.users.UserContext;

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
