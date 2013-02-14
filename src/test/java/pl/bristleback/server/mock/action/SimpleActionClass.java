package pl.bristleback.server.mock.action;

import org.apache.log4j.Logger;
import pl.bristleback.server.bristle.api.action.DefaultAction;
import pl.bristleback.server.bristle.api.annotations.Action;
import pl.bristleback.server.bristle.api.annotations.ActionClass;
import pl.bristleback.server.bristle.api.annotations.ObjectSender;
import pl.bristleback.server.bristle.authorisation.interceptor.Authorized;
import pl.bristleback.server.bristle.engine.base.users.DefaultUser;
import pl.bristleback.server.bristle.message.ConditionObjectSender;
import pl.bristleback.server.bristle.serialization.system.annotation.Bind;
import pl.bristleback.server.bristle.serialization.system.annotation.Property;
import pl.bristleback.server.bristle.serialization.system.annotation.Serialize;
import pl.bristleback.server.bristle.serialization.system.annotation.SerializeBundle;
import pl.bristleback.server.mock.beans.MockBean;
import pl.bristleback.server.mock.beans.SimpleMockBean;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-05-04 10:36:46 <br/>
 *
 * @author Wojciech Niemiec
 */
@ActionClass(name = SimpleActionClass.NAME)
@Authorized("test")
public class SimpleActionClass implements DefaultAction<DefaultUser, String> {

  private static Logger log = Logger.getLogger(SimpleActionClass.class.getName());

  public static final String RESPONSE_TEXT = "response";

  public static final String NAME = "sampleAction";

  @ObjectSender
  @SerializeBundle({
    @Serialize(target = MockBean.class),
    @Serialize(target = SimpleMockBean.class, serializationName = "nonDefaultSerialization")
  })
  private ConditionObjectSender conditionObjectSender;

  @Override
  @Action
  public String executeDefault(DefaultUser user, String message) {
    return RESPONSE_TEXT;
  }

  @Action(name = "unusualActionName")
  @Serialize(properties = {
    @Property(name = "mockBean", required = true)
  })
  @Authorized("test")
  public SimpleMockBean nonDefaultAction(@Bind(required = true) String param1) {
    return null;
  }

  @Action(name = "unusualActionNameWithBind")
  public String nonDefaultActionWithBind(@Bind(required = true, properties = {
    @Property(name = "property1", required = true)
  }) SimpleMockBean param1) {
    return "response";
  }
}
