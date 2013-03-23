package sample.outgoing;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.api.action.SendCondition;
import pl.bristleback.server.bristle.api.annotations.ClientAction;
import pl.bristleback.server.bristle.api.annotations.ClientActionClass;
import pl.bristleback.server.bristle.api.annotations.Ignore;
import pl.bristleback.server.bristle.api.users.UserContext;
import pl.bristleback.server.bristle.security.authorisation.conditions.AllUsersCondition;
import pl.bristleback.server.bristle.serialization.system.annotation.Bind;
import sample.Card;

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
      public boolean isApplicable(UserContext user) {
        return true;
      }
    };
  }

  @ClientAction("sendCardsToUser")
  public UserContext sendCardsToUser(@Bind Card[] cards, @Ignore UserContext user) {
    return user;
  }

  @ClientAction
  public SendCondition notification(@Ignore boolean isTrue, int ss, String sss) {
    return AllUsersCondition.getInstance();
  }
}
