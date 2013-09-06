package pl.bristleback.test.action;

import org.springframework.stereotype.Component;
import pl.bristleback.common.serialization.message.BristleMessage;
import pl.bristleback.server.bristle.api.action.DefaultAction;
import pl.bristleback.server.bristle.api.annotations.Action;
import pl.bristleback.server.bristle.api.annotations.ActionClass;
import pl.bristleback.server.bristle.api.annotations.Intercept;
import pl.bristleback.server.bristle.api.annotations.ObjectSender;
import pl.bristleback.server.bristle.api.users.UserContext;
import pl.bristleback.server.bristle.engine.user.BaseUserContext;
import pl.bristleback.server.bristle.message.ConditionObjectSender;
import pl.bristleback.test.Card;
import pl.bristleback.test.User;
import pl.bristleback.test.interceptor.SampleInterceptor;
import pl.bristleback.test.outgoing.SampleClientActionClass;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
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
  public void sayHello(@Size(min = 10) String name, @Min(10) int age, BaseUserContext user) throws Exception {
    BristleMessage<User> message = new BristleMessage<User>();
    User userData = new User();
    userData.setAge(age);
    userData.setFirstName(name);
    message.withName("SampleClientActionClass.userDetails").withPayload(userData);
    sender.sendMessage(message, Collections.<UserContext>singletonList(user));
  }

  @Action
  public String executeDefault(BaseUserContext userContext, Map<String, BigDecimal> message) {
    String helloWorld = "Hello, magic number is " + message.get("mapField");
    clientActionClass.sendCardsToUser(Card.values(), userContext);
    clientActionClass.notification(true, 1, "df");
    return helloWorld;
  }

  @Action
  public List<User> getFactorials(@Valid @Min(1) @Max(10) int size) {
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
