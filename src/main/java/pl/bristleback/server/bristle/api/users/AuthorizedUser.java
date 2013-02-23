package pl.bristleback.server.bristle.api.users;

/**
 * //@todo class description
 * <p/>
 * Created on: 17.02.13 13:46 <br/>
 *
 * @author Wojciech Niemiec
 */
public interface AuthorizedUser {

  String[] getAuthorities();
}
