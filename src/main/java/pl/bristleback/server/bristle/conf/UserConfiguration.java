package pl.bristleback.server.bristle.conf;

import pl.bristleback.server.bristle.api.users.UserContext;
import pl.bristleback.server.bristle.api.users.UserContextFactory;
import pl.bristleback.server.bristle.security.UsersContainer;

/**
 * //@todo class description
 * <p/>
 * Created on: 17.02.13 14:02 <br/>
 *
 * @author Wojciech Niemiec
 */
public class UserConfiguration {

  private Class<? extends UserContext> userContextClass;

  private UserContextFactory userContextFactory;

  private UsersContainer usersContainer;


  public UserConfiguration(UserContextFactory userContextFactory, Class<? extends UserContext> userContextClass, UsersContainer usersContainer) {
    this.userContextFactory = userContextFactory;
    this.userContextClass = userContextClass;
    this.usersContainer = usersContainer;
  }

  public Class<? extends UserContext> getUserContextClass() {
    return userContextClass;
  }

  public UserContextFactory getUserContextFactory() {
    return userContextFactory;
  }

  public UsersContainer getUsersContainer() {
    return usersContainer;
  }
}
