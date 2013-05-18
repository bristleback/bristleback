package pl.bristleback.server.mock.action;

import org.apache.log4j.Logger;
import pl.bristleback.server.bristle.api.action.DefaultAction;
import pl.bristleback.server.bristle.engine.user.BaseUserContext;

public class NotAnnotatedActionClass implements DefaultAction<BaseUserContext, Integer> {

  private static Logger log = Logger.getLogger(NotAnnotatedActionClass.class.getName());

  @Override
  public Integer executeDefault(BaseUserContext userContext, Integer message) {
    return -1;
  }
}
