package pl.bristleback.server.mock.action;

import org.apache.log4j.Logger;
import pl.bristleback.server.bristle.api.action.DefaultAction;
import pl.bristleback.server.bristle.engine.user.BaseUserContext;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-05-04 10:40:56 <br/>
 *
 * @author Wojciech Niemiec
 */
public class NotAnnotatedActionClass implements DefaultAction<BaseUserContext, Integer> {

  private static Logger log = Logger.getLogger(NotAnnotatedActionClass.class.getName());

  @Override
  public Integer executeDefault(BaseUserContext userContext, Integer message) {
    return -1;
  }
}
