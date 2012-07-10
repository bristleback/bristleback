package sample.outgoing;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.api.annotations.ClientAction;
import pl.bristleback.server.bristle.api.annotations.ClientActionClass;
import pl.bristleback.server.bristle.api.users.IdentifiedUser;
import pl.bristleback.server.bristle.authorisation.conditions.SendCondition;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-05-28 21:16:46 <br/>
 *
 * @author Wojciech Niemiec
 */
@ClientActionClass
@Component
public class SampleClientActionClass {
  private static Logger log = Logger.getLogger(SampleClientActionClass.class.getName());

  @ClientAction("sendGame")
  public SendCondition sendGameToAll(String gameName, int actualConnectionsNumber) {
    return new SendCondition() {
      @Override
      public boolean isApplicable(IdentifiedUser user) {
        return true;
      }
    };
  }

  @ClientAction("sendGameToUser")
  public IdentifiedUser sendGameToUser(String gameName, IdentifiedUser user, int actualConnectionsNumber) {
    return user;
  }
}
