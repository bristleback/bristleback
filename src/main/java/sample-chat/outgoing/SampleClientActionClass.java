package sample.outgoing;

import org.apache.log4j.Logger;
import pl.bristleback.server.bristle.api.annotations.ClientAction;
import pl.bristleback.server.bristle.api.annotations.Outgoing;
import pl.bristleback.server.bristle.api.users.IdentifiedUser;
import pl.bristleback.server.bristle.authorisation.conditions.SendCondition;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-05-28 21:16:46 <br/>
 *
 * @author Wojciech Niemiec
 */
@Outgoing
public class SampleClientActionClass {
  private static Logger log = Logger.getLogger(SampleClientActionClass.class.getName());

  @ClientAction("sendGame")
  public SendCondition sendGameToAll(String gameName) {
    return new SendCondition() {
      @Override
      public boolean isApplicable(IdentifiedUser user) {
        return true;
      }
    };
  }
}
