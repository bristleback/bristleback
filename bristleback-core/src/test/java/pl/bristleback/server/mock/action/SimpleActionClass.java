package pl.bristleback.server.mock.action;

import pl.bristleback.server.bristle.api.action.DefaultAction;
import pl.bristleback.server.bristle.api.annotations.Action;
import pl.bristleback.server.bristle.api.annotations.ActionClass;
import pl.bristleback.server.bristle.api.annotations.Authorized;
import pl.bristleback.server.bristle.api.annotations.ObjectSender;
import pl.bristleback.server.bristle.engine.user.BaseUserContext;
import pl.bristleback.server.bristle.message.ConditionObjectSender;
import pl.bristleback.server.bristle.serialization.system.annotation.Bind;
import pl.bristleback.server.bristle.serialization.system.annotation.Property;
import pl.bristleback.server.bristle.serialization.system.annotation.Serialize;
import pl.bristleback.server.bristle.serialization.system.annotation.SerializeBundle;
import pl.bristleback.server.mock.beans.MockBean;
import pl.bristleback.server.mock.beans.SimpleMockBean;

@ActionClass(name = SimpleActionClass.NAME)
@Authorized("test")
public class SimpleActionClass implements DefaultAction<BaseUserContext, String> {

  public static final String RESPONSE_TEXT = "response";

  public static final String NAME = "sampleAction";

  @ObjectSender
  @SerializeBundle({
    @Serialize(target = MockBean.class),
    @Serialize(target = SimpleMockBean.class)
  })
  private ConditionObjectSender conditionObjectSender;

  @Override
  @Action
  public String executeDefault(BaseUserContext userContext, String message) {
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
