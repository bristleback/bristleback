package pl.bristleback.test.action;

import org.springframework.stereotype.Controller;
import pl.bristleback.common.serialization.message.BristleMessage;
import pl.bristleback.server.bristle.api.action.DefaultAction;
import pl.bristleback.server.bristle.api.annotations.Action;
import pl.bristleback.server.bristle.api.annotations.ActionClass;
import pl.bristleback.server.bristle.api.annotations.ObjectSender;
import pl.bristleback.server.bristle.engine.user.BaseUserContext;
import pl.bristleback.server.bristle.message.ConditionObjectSender;
import pl.bristleback.server.bristle.security.authorisation.conditions.AllUsersCondition;
import pl.bristleback.test.User;

import java.math.BigDecimal;
import java.util.Map;

@Controller
@ActionClass(name = "HelloWorld")
public class HelloWorldAction implements DefaultAction<BaseUserContext, Map<String, BigDecimal>> {

  @ObjectSender
  private ConditionObjectSender conditionObjectSender;

  private HelloWorldClientAction helloWorldClientAction;

  @Action
  public User executeDefault(BaseUserContext userContext, Map<String, BigDecimal> message) {
    User user = new User();
    user.setAge(message.get("key").intValue());
    user.setFirstName("John");

    return user;
  }

  @Action
  public User actionWithObjectSender(Map<String, BigDecimal> message) throws Exception {
    User user = new User();
    user.setAge(message.get("key").intValue());
    user.setFirstName("John");

    BristleMessage<User> serverMsg = new BristleMessage<User>().withName("ClientAction.someAction").withPayload(user);
    conditionObjectSender.sendMessage(serverMsg, AllUsersCondition.getInstance());

    return user;
  }

  @Action
  public void broadcast(String hello) {
    helloWorldClientAction.sayHelloToAll(hello);
  }

  public void setHelloWorldClientAction(HelloWorldClientAction helloWorldClientAction) {
    this.helloWorldClientAction = helloWorldClientAction;
  }

}
