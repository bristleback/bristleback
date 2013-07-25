package sample.action;

import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.api.action.DefaultAction;
import pl.bristleback.server.bristle.api.annotations.Action;
import pl.bristleback.server.bristle.api.annotations.ActionClass;
import pl.bristleback.server.bristle.api.annotations.Intercept;
import pl.bristleback.server.bristle.api.annotations.ObjectSender;
import pl.bristleback.server.bristle.api.users.UserContext;
import pl.bristleback.server.bristle.engine.user.BaseUserContext;
import pl.bristleback.server.bristle.message.BristleMessage;
import pl.bristleback.server.bristle.message.ConditionObjectSender;
import sample.Card;
import sample.User;
import sample.action.interceptor.SampleInterceptor;
import sample.outgoing.SampleClientActionClass;
import sample.service.HelloServiceBean;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@ActionClass(name = "sample")
@Component
@Intercept(SampleInterceptor.class)
public class SampleAction implements DefaultAction<BaseUserContext, Map<String, BigDecimal>> {

  @ObjectSender
  private ConditionObjectSender sender;

  @Inject
  private HelloServiceBean helloServiceBean;

  @Inject
  private SampleClientActionClass clientActionClass;

  @Action(name = "customName")
  public User changeAge(int newAge, User user) {
    user.setAge(newAge);
    return user;
  }

  @Action(name = "myActionName")
  public User changeUserAge(User user, int age) {
    user.setAge(age);
    return user;
  }

  @Action(name = "hello")
  public void sayHello(@Valid @NotNull String name, int age, BaseUserContext user) throws Exception {
    BristleMessage<User> message = new BristleMessage<User>();
    User userData = new User();
    userData.setAge(age);
    userData.setFirstName(name);
    message.withName("SampleClientActionClass.userDetails").withPayload(userData);
    sender.sendMessage(message, Collections.<UserContext>singletonList(user));
  }

  @Action
  public String executeDefault(BaseUserContext userContext, Map<String, BigDecimal> message) {
    String helloWorld = helloServiceBean.sayHello(message.get("mapField"));
    clientActionClass.sendCardsToUser(Card.values(), userContext);
    clientActionClass.notification(true, 1, "df");
    return helloWorld;
  }

  @Action
  public List<User> getFactorials(int size) {
    if (size < 0) {
      throw new RuntimeException();
    }
    if (size > 10) {
      throw new IllegalArgumentException("This would be too high number");
    }
    List<User> returnedList = new ArrayList<User>(size);
    int result = 1;
    for (int i = 1; i <= size; i++) {
      result = result * i;
      User user = new User();
      user.setFriend(user);
      user.setAge(result);
      returnedList.add(user);
    }
    return returnedList;
  }
}