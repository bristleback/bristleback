package sample.action;

import org.springframework.stereotype.Controller;
import pl.bristleback.server.bristle.api.action.DefaultAction;
import pl.bristleback.server.bristle.api.annotations.Action;
import pl.bristleback.server.bristle.api.annotations.ActionClass;
import pl.bristleback.server.bristle.engine.user.BaseUserContext;
import sample.User;

@Controller
@ActionClass(name = "HelloWorld")
public class HelloWorldAction implements DefaultAction<BaseUserContext, String> {

  @Action
  public User executeDefault(BaseUserContext userContext, String name) {
    User user = new User();
    user.setAge(20);
    user.setFirstName(name);

    return user;
  }

}
