package pl.bristleback.server.mock.action;

import org.apache.log4j.Logger;
import pl.bristleback.server.bristle.api.action.DefaultAction;
import pl.bristleback.server.bristle.api.annotations.Action;
import pl.bristleback.server.bristle.api.annotations.ActionClass;
import pl.bristleback.server.bristle.api.annotations.Bind;
import pl.bristleback.server.bristle.api.annotations.Property;
import pl.bristleback.server.bristle.api.annotations.Serialize;
import pl.bristleback.server.bristle.engine.base.users.DefaultUser;
import pl.bristleback.server.mock.beans.SimpleMockBean;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-05-04 10:36:46 <br/>
 *
 * @author Wojciech Niemiec
 */
@ActionClass(name = SimpleActionClass.NAME)
public class SimpleActionClass implements DefaultAction<DefaultUser, String> {
  private static Logger log = Logger.getLogger(SimpleActionClass.class.getName());

  public static final String RESPONSE_TEXT = "response";

  public static final String NAME = "sampleAction";

  @Override
  @Action
  public String executeDefault(DefaultUser user, String message) {
    return RESPONSE_TEXT;
  }

  @Action(name = "unusualActionName", response = {
    @Serialize(properties = {
      @Property(name = "mockBean", required = true)
    }),
    @Serialize(required = true, serializationName = "someField")
  })
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
