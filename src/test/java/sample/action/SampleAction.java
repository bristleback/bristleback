package sample.action;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.api.action.DefaultAction;
import pl.bristleback.server.bristle.api.annotations.Action;
import pl.bristleback.server.bristle.api.annotations.ActionClass;
import pl.bristleback.server.bristle.api.annotations.Intercept;
import pl.bristleback.server.bristle.api.annotations.ObjectSender;
import pl.bristleback.server.bristle.api.users.IdentifiedUser;
import pl.bristleback.server.bristle.engine.base.users.DefaultUser;
import pl.bristleback.server.bristle.message.BristleMessage;
import pl.bristleback.server.bristle.message.ConditionObjectSender;
import pl.bristleback.server.bristle.serialization.system.annotation.Bind;
import pl.bristleback.server.bristle.serialization.system.annotation.Property;
import pl.bristleback.server.bristle.serialization.system.annotation.Serialize;
import sample.Card;
import sample.User;
import sample.action.interceptor.SampleInterceptor;
import sample.outgoing.SampleClientActionClass;
import sample.service.HelloServiceBean;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-07-21 15:33:23 <br/>
 *
 * @author Wojciech Niemiec
 */

@ActionClass(name = "sample")
@Component
@Intercept(SampleInterceptor.class)
public class SampleAction implements DefaultAction<DefaultUser, Map<String, BigDecimal>> {

  private static Logger log = Logger.getLogger(SampleAction.class.getName());

  @ObjectSender
  @Serialize(target = User.class, properties = {
    @Property(name = "friend", skipped = true)
  })
  private ConditionObjectSender sender;

  @Inject
  private HelloServiceBean helloServiceBean;

  @Inject
  private SampleClientActionClass clientActionClass;

  @Action(name = "customName")
  @Serialize(properties = {
    @Property(name = "friend", skipped = true)
  })
  public User changeAge(int newAge,
                        @Bind(properties = {
                          @Property(name = "age", skipped = true)
                        }) User user) {
    user.setAge(newAge);
    return user;
  }

  @Action(name = "myActionName")
  public User changeUserAge(User user, @Bind(required = true) int age) {
    user.setAge(age);
    return user;
  }

  @Action(name = "hello")
  public void sayHello(@Bind(required = true) String name, int age, DefaultUser user) throws Exception {
    BristleMessage<User> message = new BristleMessage<User>();
    User userData = new User();
    userData.setAge(age);
    userData.setFirstName(name);
    message.withName("SampleClientActionClass.userDetails").withPayload(userData);
    sender.sendMessage(message, Collections.<IdentifiedUser>singletonList(user));
  }

  @Action
  public String executeDefault(DefaultUser user, Map<String, BigDecimal> message) {
    String helloWorld = helloServiceBean.sayHello(message.get("mapField"));
    clientActionClass.sendCardsToUser(Card.values(), user);
    clientActionClass.notification(true, 1, "df");
    return helloWorld;
  }

  @Action
  @Serialize(serializationName = "factorials", containerElementClass = User.class, properties = {
    @Property(name = "friend", skipped = true)
  })
  public List<User> getFactorials(@Bind(required = true) int size) {
    if (size < 0 || size > 10) {
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
