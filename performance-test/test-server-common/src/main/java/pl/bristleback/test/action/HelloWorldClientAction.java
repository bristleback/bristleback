package pl.bristleback.test.action;

import pl.bristleback.server.bristle.api.action.SendCondition;
import pl.bristleback.server.bristle.api.annotations.ClientAction;
import pl.bristleback.server.bristle.api.annotations.ClientActionClass;
import pl.bristleback.server.bristle.security.authorisation.conditions.AllUsersCondition;

@ClientActionClass
public class HelloWorldClientAction {

  @ClientAction
  public SendCondition sayHelloToAll(String hello) {
    return AllUsersCondition.getInstance();
  }
}
