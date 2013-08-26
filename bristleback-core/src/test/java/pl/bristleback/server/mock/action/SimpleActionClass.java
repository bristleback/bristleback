package pl.bristleback.server.mock.action;

import pl.bristleback.server.bristle.api.action.DefaultAction;
import pl.bristleback.server.bristle.api.annotations.Action;
import pl.bristleback.server.bristle.api.annotations.ActionClass;
import pl.bristleback.server.bristle.api.annotations.Authorized;
import pl.bristleback.server.bristle.api.annotations.ObjectSender;
import pl.bristleback.server.bristle.engine.user.BaseUserContext;
import pl.bristleback.server.bristle.message.ConditionObjectSender;
import pl.bristleback.server.mock.beans.SimpleMockBean;

@ActionClass(name = SimpleActionClass.NAME)
@Authorized("test")
public class SimpleActionClass implements DefaultAction<BaseUserContext, String> {

  public static final String RESPONSE_TEXT = "response";

  public static final String NAME = "sampleAction";

  @ObjectSender
  private ConditionObjectSender conditionObjectSender;

  @Override
  @Action
  public String executeDefault(BaseUserContext userContext, String message) {
    return RESPONSE_TEXT;
  }

  @Action(name = "unusualActionName")
  @Authorized("test")
  public SimpleMockBean nonDefaultAction(String param1) {
    return null;
  }

  @Action(name = "unusualActionNameWithBind")
  public String nonDefaultActionWithBind(SimpleMockBean param1) {
    return "response";
  }
}
