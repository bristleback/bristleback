package pl.bristleback.server.bristle.api;

import pl.bristleback.server.bristle.api.users.IdentifiedUser;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-11-20 14:47:18 <br/>
 *
 * @author Wojciech Niemiec
 */
public interface ConnectionStateListener<T extends IdentifiedUser> {

  void userConnected(T identifiedUser);

  void userDisconnected(T identifiedUser);
}
