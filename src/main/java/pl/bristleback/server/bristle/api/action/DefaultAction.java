package pl.bristleback.server.bristle.api.action;

import pl.bristleback.server.bristle.api.users.IdentifiedUser;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-07-20 12:29:18 <br/>
 *
 * @author Wojciech Niemiec
 */
public interface DefaultAction<U extends IdentifiedUser, T> {

  Object executeDefault(U user, T payload);
} 
