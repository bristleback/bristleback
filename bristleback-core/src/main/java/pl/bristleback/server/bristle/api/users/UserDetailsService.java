package pl.bristleback.server.bristle.api.users;

/**
 * //@todo class description
 * <p/>
 * Created on: 19.02.13 22:44 <br/>
 *
 * @author Wojciech Niemiec
 */
public interface UserDetailsService {

  UserDetails getUserDetails(String username);
}
